let autoTradeInterval;

async function fetchStatus() {
    const statusResponse = await fetch('/status');
    const status = await statusResponse.json();
    document.getElementById('bitcoinAmount').innerText = status.bitcoinAmount.toFixed(8);
    document.getElementById('currentValue').innerText = status.currentValue.toFixed(2);
    document.getElementById('profitOrLoss').innerText = status.profitOrLoss.toFixed(2);
    document.getElementById('funds').innerText = status.funds.toFixed(2);
    document.getElementById('bitcoinPrice').innerText = status.currentPrice.toFixed(2);

    const historyResponse = await fetch('/tradeHistory');
    const history = await historyResponse.json();
    const tradeList = document.getElementById('tradeList');
    tradeList.innerHTML = '';
    history.forEach(entry => {
        const listItem = document.createElement('li');
        listItem.textContent = entry;
        tradeList.appendChild(listItem);
    });
}

async function buyBitcoin() {
    const amount = document.getElementById('investmentAmount').value;
    const response = await fetch('/buy', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ amount: parseFloat(amount) })
    });
    const message = await response.text();
    alert(message);
    fetchStatus();
}

async function sellBitcoin() {
    const response = await fetch('/sell', { method: 'POST' });
    const message = await response.text();
    alert(message);
    fetchStatus();
}

async function autoTrade() {
    const response = await fetch('/autoTrade', { method: 'POST' });
    const message = await response.text();
    console.log(message);
    fetchStatus();
}

function toggleAutoTrade() {
    if (autoTradeInterval) {
        clearInterval(autoTradeInterval);
        autoTradeInterval = null;
        alert('Auto Trade Stopped');
    } else {
        autoTradeInterval = setInterval(autoTrade, 1000);
        alert('Auto Trade Started');
    }
}

fetchStatus();
setInterval(fetchStatus, 1000);
