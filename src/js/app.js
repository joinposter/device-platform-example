import 'babel-polyfill';

// Will show button to send message
Poster.interface.showApplicationIconAt({
    functions: 'Say 👋',
    order: 'Say 👋',
});


// Will send message to device
Poster.on('applicationIconClicked', async () => {
    let devices = await Poster.devices.getAll() || [];

    devices.forEach((device) => {
        device.sendMessage({
            text: 'Hello, World!',
            terminalId: Poster.settings.accountUrl + Poster.settings.spotTabletId
        });
    });
});


// Show notification on message from Device
Poster.on('deviceMessage', (data) => {
    const { device, message } = data;

    alert(JSON.stringify(data));

    Poster.interface.showNotification({
        title: 'Device Message',
        message: message.text,
        icon: 'https://dev.joinposter.com/public/apps/image.png',
    });
});
