import json
from pathlib import Path

CONFIG_DIR = Path(__file__).parent


def load_config(name: str) -> dict:
    with open(CONFIG_DIR / f"{name}.json", "r", encoding="utf-8") as f:
        return json.load(f)