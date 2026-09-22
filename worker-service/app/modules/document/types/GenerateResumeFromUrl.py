from pydantic import BaseModel
from app.common.config.PydanticCamelCaseModel import PydanticCamelCaseModel
from app.modules.profile.types.Profile import Profile

class GenerateResumeFromUrl(PydanticCamelCaseModel):
    url: str
    profile: Profile
    

class GenerateResumeFromDescription(PydanticCamelCaseModel):
    description: str
    profile: Profile