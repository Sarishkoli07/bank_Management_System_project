/* =====================================================
   Bank Management System - frontend JavaScript
   It talks to the Spring Boot REST API using fetch().
   ===================================================== */

const API = "/api/accounts";

// ---------- small helper functions ----------

function $(id) {
    return document.getElementById(id);
}

/** Converts special characters so that user text can never run as HTML (prevents XSS). */
function esc(value) {
    const div = document.createElement("div");
    div.textContent = value === null || value === undefined ? "" : String(value);
    return div.innerHTML;
}

function money(value) {
    return "Rs. " + Number(value).toLocaleString("en-IN", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}

function dateTime(value) {
    // keep only yyyy-MM-ddTHH:mm:ss so every browser can read it
    return new Date(String(value).substring(0, 19)).toLocaleString("en-IN");
}

let messageTimer = null;

function showMessage(text, type) {
    const box = $("message");
    box.textContent = text;
    box.className = "message " + type; // "success" or "error"
    clearTimeout(messageTimer);
    messageTimer = setTimeout(() => box.classList.add("hidden"), 8000);
    window.scrollTo({top: 0, behavior: "smooth"});
}

/** Calls the server. Throws an Error with a readable message if the server says "error". */
async function api(url, options) {
    let response;
    try {
        response = await fetch(url, {
            headers: {"Content-Type": "application/json"},
            ...options
        });
    } catch (e) {
        throw new Error("Cannot connect to the server. Is the Spring Boot application running?");
    }

    let data = null;
    try {
        data = await response.json();
    } catch (e) {
        // response had no JSON body
    }

    if (!response.ok) {
        let message = data && data.message ? data.message : "Request failed (status " + response.status + ")";
        if (data && data.errors && data.errors.length > 0) {
            message += ": " + data.errors.join(", ");
        }
        throw new Error(message);
    }
    return data;
}

/** Disables a button while the request is running so the user cannot click twice. */
async function withButton(button, work) {
    button.disabled = true;
    try {
        await work();
    } catch (e) {
        showMessage(e.message, "error");
    } finally {
        button.disabled = false;
    }
}

function isAccountNumber(value) {
    return /^[0-9]{10}$/.test(value);
}

function typeTag(type) {
    return '<span class="tag tag-' + esc(type.toLowerCase()) + '">' + esc(type) + "</span>";
}

function accountRows(accounts) {
    if (accounts.length === 0) {
        return '<tr><td colspan="5" class="empty">No accounts found.</td></tr>';
    }
    return accounts.map(a =>
        "<tr>" +
        "<td>" + esc(a.accountNumber) + "</td>" +
        "<td>" + esc(a.holderName) + "</td>" +
        "<td>" + typeTag(a.accountType) + "</td>" +
        "<td>" + esc(a.phone) + "</td>" +
        '<td class="num">' + esc(money(a.balance)) + "</td>" +
        "</tr>"
    ).join("");
}

// ---------- menu: show one panel at a time ----------

document.querySelectorAll(".menu-btn").forEach(button => {
    button.addEventListener("click", () => {
        document.querySelectorAll(".menu-btn").forEach(b => b.classList.remove("active"));
        document.querySelectorAll(".panel").forEach(p => p.classList.remove("active"));
        button.classList.add("active");
        $(button.dataset.target).classList.add("active");
        $("message").classList.add("hidden");
        if (button.dataset.target === "accounts") {
            loadAccounts();
        }
        if (button.dataset.target === "graphs") {
            loadAccountGraphs();
        }
    });
});

// ---------- 1. All accounts ----------

async function loadAccounts() {
    try {
        const accounts = await api(API);
        $("accountsBody").innerHTML = accountRows(accounts);
    } catch (e) {
        showMessage(e.message, "error");
    }
}

$("refreshBtn").addEventListener("click", loadAccounts);

// ---------- 2. Create account ----------

$("createForm").addEventListener("submit", event => {
    event.preventDefault();

    const body = {
        holderName: $("holderName").value.trim(),
        email: $("email").value.trim(),
        phone: $("phone").value.trim(),
        accountType: $("accountType").value,
        initialDeposit: $("initialDeposit").value
    };

    // Client-side validation (quick feedback). The server validates again.
    if (body.holderName.length < 3 || !/^[A-Za-z ]+$/.test(body.holderName)) {
        return showMessage("Name must be at least 3 characters and contain only letters and spaces", "error");
    }
    if (!/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(body.email)) {
        return showMessage("Enter a valid email address", "error");
    }
    if (!/^[6-9][0-9]{9}$/.test(body.phone)) {
        return showMessage("Phone must be 10 digits and start with 6, 7, 8 or 9", "error");
    }
    if (body.initialDeposit === "" || Number(body.initialDeposit) < 0) {
        return showMessage("Enter a valid opening deposit (0 or more)", "error");
    }
    body.initialDeposit = Number(body.initialDeposit);

    withButton(event.submitter || $("createForm").querySelector("button"), async () => {
        const account = await api(API, {method: "POST", body: JSON.stringify(body)});
        showMessage("Account created. Account number: " + account.accountNumber +
            " (" + account.accountType + "). Balance: " + money(account.balance), "success");
        $("createForm").reset();
    });
});

// ---------- 3. Deposit / Withdraw ----------

function transact(kind, button) {
    const accountNumber = $("txnAccount").value.trim();
    const amount = $("txnAmount").value;

    if (!isAccountNumber(accountNumber)) {
        return showMessage("Account number must be exactly 10 digits", "error");
    }
    if (amount === "" || Number(amount) <= 0) {
        return showMessage("Enter an amount greater than zero", "error");
    }

    withButton(button, async () => {
        const txn = await api(API + "/" + encodeURIComponent(accountNumber) + "/" + kind, {
            method: "POST",
            body: JSON.stringify({amount: Number(amount)})
        });
        const word = kind === "deposit" ? "Deposit" : "Withdrawal";
        showMessage(word + " of " + money(txn.amount) + " successful. New balance: " +
            money(txn.balanceAfter), "success");
        $("txnAmount").value = "";
    });
}

$("depositBtn").addEventListener("click", () => transact("deposit", $("depositBtn")));
$("withdrawBtn").addEventListener("click", () => transact("withdraw", $("withdrawBtn")));

// ---------- 4. Check balance ----------

$("balanceBtn").addEventListener("click", () => {
    const accountNumber = $("balAccount").value.trim();
    if (!isAccountNumber(accountNumber)) {
        return showMessage("Account number must be exactly 10 digits", "error");
    }

    withButton($("balanceBtn"), async () => {
        const a = await api(API + "/" + encodeURIComponent(accountNumber));
        $("balanceResult").innerHTML =
            '<div class="passbook">' +
            '<div class="pb-name">' + esc(a.holderName) + "</div>" +
            '<div class="pb-sub">Account ' + esc(a.accountNumber) + " &middot; " + esc(a.accountType) + "</div>" +
            '<div class="pb-balance">' + esc(money(a.balance)) + "</div>" +
            "<dl>" +
            "<dt>Minimum balance</dt><dd>" + esc(money(a.minimumBalance)) + "</dd>" +
            "<dt>Monthly interest</dt><dd>" + esc(money(a.monthlyInterest)) + "</dd>" +
            "<dt>Email</dt><dd>" + esc(a.email) + "</dd>" +
            "<dt>Phone</dt><dd>" + esc(a.phone) + "</dd>" +
            "<dt>Opened on</dt><dd>" + esc(dateTime(a.createdAt)) + "</dd>" +
            "</dl></div>";
    });
});

// ---------- 5. Search ----------

$("searchBtn").addEventListener("click", () => {
    const keyword = $("keyword").value.trim();
    if (keyword === "") {
        return showMessage("Enter a name, account number or phone to search", "error");
    }

    withButton($("searchBtn"), async () => {
        const accounts = await api(API + "/search?keyword=" + encodeURIComponent(keyword));
        $("searchBody").innerHTML = accountRows(accounts);
    });
});

// ---------- 6. Transaction history ----------

$("historyBtn").addEventListener("click", () => {
    const accountNumber = $("histAccount").value.trim();
    if (!isAccountNumber(accountNumber)) {
        return showMessage("Account number must be exactly 10 digits", "error");
    }

    withButton($("historyBtn"), async () => {
        const list = await api(API + "/" + encodeURIComponent(accountNumber) + "/transactions");
        if (list.length === 0) {
            $("historyBody").innerHTML = '<tr><td colspan="5" class="empty">No transactions yet.</td></tr>';
            return;
        }
        $("historyBody").innerHTML = list.map(t =>
            "<tr>" +
            "<td>" + esc(dateTime(t.transactionTime)) + "</td>" +
            '<td><span class="tag tag-' + esc(t.type.toLowerCase()) + '">' + esc(t.type) + "</span></td>" +
            "<td>" + esc(t.description) + "</td>" +
            '<td class="num">' + esc(money(t.amount)) + "</td>" +
            '<td class="num">' + esc(money(t.balanceAfter)) + "</td>" +
            "</tr>"
        ).join("");
    });
});

// ---------- 7. Graphs (Computer Graphics part) ----------
/*
  Everything below is drawn by hand with the HTML5 Canvas 2D API. No chart library is used.
  Computer Graphics ideas used:
   - Coordinate system: canvas origin (0,0) is the TOP-LEFT corner and y grows DOWNWARD,
     so a value is converted to a screen y using a scaling (mapping) formula.
   - Primitives: lines (moveTo/lineTo), rectangles (fillRect), arcs (arc), circles, text.
   - Colors, fill and stroke styles, line width, dashed lines (setLineDash).
   - Pie chart: angle = (value / total) * 2 * PI.
*/

const COLORS = {teal: "#0d4a55", gold: "#e8a317", green: "#1c7a4a", red: "#b3342d", grid: "#d5dbe0", text: "#17242b"};

function prepareCanvas(id) {
    const canvas = $(id);
    const ctx = canvas.getContext("2d");
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.font = "13px Segoe UI, Arial, sans-serif";
    ctx.textBaseline = "middle";
    return {canvas, ctx};
}

function drawMessage(ctx, canvas, text) {
    ctx.fillStyle = "#5d6b73";
    ctx.textAlign = "center";
    ctx.fillText(text, canvas.width / 2, canvas.height / 2);
}

/** Draws the X/Y axes, horizontal grid lines and Y labels. Returns a function that maps a value to screen y. */
function drawAxes(ctx, area, min, max) {
    const toY = value => area.top + (max - value) / (max - min) * area.height;
    const steps = 4;
    ctx.lineWidth = 1;
    ctx.textAlign = "right";
    for (let i = 0; i <= steps; i++) {
        const value = min + (max - min) * i / steps;
        const y = toY(value);
        ctx.strokeStyle = COLORS.grid;
        ctx.setLineDash([4, 4]);
        ctx.beginPath();
        ctx.moveTo(area.left, y);
        ctx.lineTo(area.left + area.width, y);
        ctx.stroke();
        ctx.setLineDash([]);
        ctx.fillStyle = COLORS.text;
        ctx.fillText(Math.round(value).toLocaleString("en-IN"), area.left - 8, y);
    }
    // main axes
    ctx.strokeStyle = COLORS.text;
    ctx.lineWidth = 2;
    ctx.beginPath();
    ctx.moveTo(area.left, area.top);
    ctx.lineTo(area.left, area.top + area.height);
    ctx.lineTo(area.left + area.width, area.top + area.height);
    ctx.stroke();
    return toY;
}

function chartRange(values) {
    let min = Math.min(0, ...values);
    let max = Math.max(0, ...values);
    if (max === min) {
        max = min + 1;
    }
    return {min, max};
}

function drawBarChart(accounts) {
    const {canvas, ctx} = prepareCanvas("barChart");
    if (accounts.length === 0) {
        return drawMessage(ctx, canvas, "No accounts yet");
    }
    const top = accounts.slice(0, 8);                       // newest 8 accounts
    const values = top.map(a => Number(a.balance));
    const {min, max} = chartRange(values);
    const area = {left: 70, top: 20, width: canvas.width - 90, height: canvas.height - 70};
    const toY = drawAxes(ctx, area, min, max);

    const slot = area.width / top.length;
    const barWidth = slot * 0.6;
    const zeroY = toY(0);
    top.forEach((a, i) => {
        const x = area.left + i * slot + (slot - barWidth) / 2;
        const y = toY(values[i]);
        ctx.fillStyle = values[i] >= 0 ? COLORS.teal : COLORS.red;
        ctx.fillRect(x, Math.min(y, zeroY), barWidth, Math.abs(zeroY - y));
        ctx.fillStyle = COLORS.text;
        ctx.textAlign = "center";
        ctx.fillText(a.holderName.split(" ")[0].substring(0, 8), x + barWidth / 2, area.top + area.height + 16);
        ctx.fillText(a.accountNumber.slice(-4), x + barWidth / 2, area.top + area.height + 34);
    });
}

function drawPieChart(accounts) {
    const {canvas, ctx} = prepareCanvas("pieChart");
    const savings = accounts.filter(a => a.accountType === "SAVINGS").length;
    const current = accounts.filter(a => a.accountType === "CURRENT").length;
    const total = savings + current;
    if (total === 0) {
        return drawMessage(ctx, canvas, "No accounts yet");
    }
    const slices = [
        {label: "Savings", value: savings, color: COLORS.teal},
        {label: "Current", value: current, color: COLORS.gold}
    ];
    const cx = 190, cy = canvas.height / 2, radius = 120;
    let start = -Math.PI / 2;                                // start at the top (12 o'clock)
    slices.forEach(s => {
        const angle = s.value / total * 2 * Math.PI;         // angle of this slice
        if (s.value > 0) {
            ctx.beginPath();
            ctx.moveTo(cx, cy);
            ctx.arc(cx, cy, radius, start, start + angle);
            ctx.closePath();
            ctx.fillStyle = s.color;
            ctx.fill();
            ctx.strokeStyle = "#fff";
            ctx.lineWidth = 2;
            ctx.stroke();
            // percentage text at the middle angle of the slice
            const mid = start + angle / 2;
            ctx.fillStyle = "#fff";
            ctx.textAlign = "center";
            ctx.fillText(Math.round(s.value / total * 100) + "%",
                cx + Math.cos(mid) * radius * 0.6, cy + Math.sin(mid) * radius * 0.6);
        }
        start += angle;
    });
    // legend
    ctx.textAlign = "left";
    slices.forEach((s, i) => {
        const y = 120 + i * 32;
        ctx.fillStyle = s.color;
        ctx.fillRect(360, y - 9, 18, 18);
        ctx.fillStyle = COLORS.text;
        ctx.fillText(s.label + ": " + s.value + " account(s)", 388, y);
    });
}

function drawLineChart(transactions) {
    const {canvas, ctx} = prepareCanvas("lineChart");
    if (transactions.length === 0) {
        return drawMessage(ctx, canvas, "No transactions to draw");
    }
    const list = transactions.slice().reverse();             // oldest first
    const values = list.map(t => Number(t.balanceAfter));
    const {min, max} = chartRange(values);
    const area = {left: 70, top: 20, width: canvas.width - 90, height: canvas.height - 60};
    const toY = drawAxes(ctx, area, min, max);
    const toX = i => list.length === 1 ? area.left + area.width / 2
        : area.left + i * area.width / (list.length - 1);

    ctx.strokeStyle = COLORS.teal;
    ctx.lineWidth = 3;
    ctx.beginPath();
    values.forEach((v, i) => (i === 0 ? ctx.moveTo(toX(i), toY(v)) : ctx.lineTo(toX(i), toY(v))));
    ctx.stroke();

    list.forEach((t, i) => {
        ctx.beginPath();
        ctx.arc(toX(i), toY(values[i]), 5, 0, 2 * Math.PI);
        ctx.fillStyle = t.type === "DEPOSIT" ? COLORS.green : COLORS.red;
        ctx.fill();
        ctx.fillStyle = COLORS.text;
        ctx.textAlign = "center";
        ctx.fillText("#" + (i + 1), toX(i), area.top + area.height + 16);
    });
    ctx.textAlign = "left";
    ctx.fillStyle = COLORS.green;
    ctx.fillText("Green = Deposit", area.left, canvas.height - 10);
    ctx.fillStyle = COLORS.red;
    ctx.fillText("Red = Withdraw", area.left + 130, canvas.height - 10);
}

async function loadAccountGraphs() {
    try {
        const accounts = await api(API);
        drawBarChart(accounts);
        drawPieChart(accounts);
    } catch (e) {
        showMessage(e.message, "error");
    }
}

$("graphRefreshBtn").addEventListener("click", loadAccountGraphs);

$("graphBtn").addEventListener("click", () => {
    const accountNumber = $("graphAccount").value.trim();
    if (!isAccountNumber(accountNumber)) {
        return showMessage("Account number must be exactly 10 digits", "error");
    }
    withButton($("graphBtn"), async () => {
        drawLineChart(await api(API + "/" + encodeURIComponent(accountNumber) + "/transactions"));
    });
});

// ---------- when the page opens ----------
loadAccounts();
