# app/modules/llm/openai_service.py

import json
from typing import Any

from openai import AsyncOpenAI

from app.common.service.llm.openai.openai_config import OPENAI_CONFIG


class OpenAIService:
    def __init__(self):
        self.client = AsyncOpenAI(
            api_key=OPENAI_CONFIG["API_KEY"],
        )

    async def chat(self, prompt: str) -> str:
        response = await self.client.responses.create(
            model=OPENAI_CONFIG["MODEL"],
            input=prompt,
        )

        return response.output_text

    async def json_response_chat(
        self,
        prompt: str,
        example_format: dict[str, Any],
    ) -> dict[str, Any]:

        response = await self.client.responses.create(
            model=OPENAI_CONFIG["MODEL"],
            input=f"""
You are a JSON API.

Return ONLY valid JSON.
Do not include markdown.
Do not include explanations.

Example format:

{json.dumps(example_format, indent=2)}

Task:

{prompt}
""",
            text={
                "format": {
                    "type": "json_object"
                }
            },
        )

        return json.loads(response.output_text)


openai_service = OpenAIService()