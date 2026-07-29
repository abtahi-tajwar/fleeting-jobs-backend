# app/modules/llm/ollama_service.py
import json

from ollama import AsyncClient

from app.common.service.llm.ollama.ollama_config import OLLAMA_CONFIG


class OllamaService:
    def __init__(self):
        self.client = AsyncClient(
            host=OLLAMA_CONFIG["HOST"]
        )

    async def chat(self, prompt: str) -> str:
        response = await self.client.chat(
            model=OLLAMA_CONFIG["MODEL"],
            messages=[
                {
                    "role": "user",
                    "content": prompt,
                }
            ],
        )

        return response["message"]["content"]

    from typing import Any

    async def json_response_chat(self, prompt: str, example_format: dict[str, Any]) -> dict[str, Any]:
        response = await self.client.chat(
            model=OLLAMA_CONFIG["MODEL"],
            messages=[
                {
                    "role": "user",
                    "content": f"""
                    You are a JSON API.
                
                    Return ONLY valid JSON.
                    Do not include markdown.
                    Do not include explanations.
                
                    Example format:
                
                    {json.dumps(example_format, indent=2)}
                
                    Task:
                
                    {prompt}
                    """
                }
            ],
        )
        return json.loads(response["message"]["content"])


ollama_service = OllamaService()
