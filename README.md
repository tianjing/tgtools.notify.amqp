# tgtools.notify.amqp
tgtools.notify.amqp

基于 tgtools.develop的websocket增加rabbitmq 实现

用户进行websocket长连接时建立消息的监听，收到消息后通过websocket转发给用户

通过 config 启动时创建系统级 queue 和 exchange

RabbitMqService 为 常用 功能