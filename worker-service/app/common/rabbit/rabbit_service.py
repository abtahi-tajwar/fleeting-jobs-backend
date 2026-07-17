import pika
import json

from app.common.rabbit.rabbit_config import HOST, PORT, USERNAME, PASSWORD


class RabbitService:
    def __init__(
        self
    ):
        self.connection = pika.BlockingConnection(
            pika.ConnectionParameters(
                host=HOST,
                port=PORT,
                credentials=pika.PlainCredentials(
                    USERNAME,
                    PASSWORD
                )
            )
        )

        self.channel = self.connection.channel()
        
    def declare_queue(self, queue_name: str, durable: bool = True):
        self.channel.queue_declare(
            queue=queue_name,
            durable=durable
        )

    def consume(self, queue_name: str, callback):
        self.channel.basic_consume(
            queue=queue_name,
            on_message_callback=callback
        )

    def start(self):
        print("RabbitMQ consumer started...")
        self.channel.start_consuming()

    def stop(self):
        if self.channel.is_open:
            self.channel.stop_consuming()

        if self.connection.is_open:
            self.connection.close()
            
    def publish(self, queue_name: str, message):
        self.channel.basic_publish(
            exchange="",
            routing_key=queue_name,
            body=json.dumps(message),
            properties=pika.BasicProperties(
                delivery_mode=2,
                content_type="application/json"
            )
        )