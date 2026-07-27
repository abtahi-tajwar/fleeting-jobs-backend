from pydantic import BaseModel

class GenerateResumeFromUrl(BaseModel):
    url: str