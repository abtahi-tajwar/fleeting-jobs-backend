import json
import aio_pika

from app.common.rabbit.rabbit_config import HOST, PORT, USERNAME, PASSWORD


class RabbitService:
    def __init__(self):
        self.connection = None
        self.channel = None

    async def connect(self):
        self.connection = await aio_pika.connect_robust(
            host=HOST,
            port=PORT,
            login=USERNAME,
            password=PASSWORD,
        )

        self.channel = await self.connection.channel()
        await self.channel.set_qos(prefetch_count=1)

    async def declare_queue(self, queue_name: str, durable: bool = True):
        return await self.channel.declare_queue(
            queue_name,
            durable=durable
        )

    async def consume(self, queue_name: str, callback):
        queue = await self.declare_queue(queue_name)

        async def wrapper(message: aio_pika.IncomingMessage):
            async with message.process():
                await callback(message)

        await queue.consume(wrapper)

    async def publish(self, queue_name: str, message):
        await self.channel.default_exchange.publish(
            aio_pika.Message(
                body=json.dumps(message).encode(),
                content_type="application/json",
                delivery_mode=aio_pika.DeliveryMode.PERSISTENT,
            ),
            routing_key=queue_name,
        )

    async def stop(self):
        if self.connection:
            await self.connection.close()

rabbit_service = RabbitService()