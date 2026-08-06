from pydantic import BaseModel

class GenerateResumeFromUrl(BaseModel):
    url: str

class GenerateResumeFromDescription(BaseModel):
    description: str