'use strict';

import '../css/styles.css';

import 'babel-polyfill';

import React from 'react';
import ReactDOM from 'react-dom';

export default class DeviceHandshakeApp extends React.Component {
    constructor(props) {
        super(props);

        // Показываем кнопки приложения в окне настроек и заказа
        Poster.interface.showApplicationIconAt({
            functions: 'Say 👋',
            order: 'Say 👋',
        });

        // Подписываемся на клик по кнопке
        Poster.on('applicationIconClicked', (data) => {
            this.sendMessage();
        });

        Poster.on('deviceMessage', (msg) => {
            Poster.interface.showNotification({
                title: 'Device Message',
                message: msg.text || 'Hi there ✨',
                icon: 'https://dev.joinposter.com/public/apps/image.png',
            })
        });
    }

    sendMessage = async () => {
        let devices = await Poster.devices.getAll() || [];
        devices.forEach((device) => {
            device.sendMessage({
                text: 'Hello, World!',
                terminalId: Poster.settings.accountUrl + Poster.settings.spotTabletId
            });
        });
    };

    render() {
        return (
            <div className="device-handshake" />
        )
    }
}

ReactDOM.render(
    <DeviceHandshakeApp />,
    document.getElementById('app-container')
);