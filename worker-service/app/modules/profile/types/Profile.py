from datetime import date
from typing import Optional

from pydantic import BaseModel, ConfigDict, Field


def to_camel(string: str) -> str:
    parts = string.split("_")
    return parts[0] + "".join(word.capitalize() for word in parts[1:])


class ProfileBaseModel(BaseModel):
    model_config = ConfigDict(
        alias_generator=to_camel,
        populate_by_name=True,
    )


class Award(ProfileBaseModel):
    id: int
    title: str
    organization: str
    date: date
    description: str


class Certification(ProfileBaseModel):
    id: int
    name: Optional[str] = None
    organization: Optional[str] = None
    issue_date: Optional[date] = None
    expiration_date: Optional[date] = None
    credential_id: Optional[str] = None
    credential_url: Optional[str] = None
    description: Optional[str] = None


class Education(ProfileBaseModel):
    id: int
    institution: str
    degree: str
    major: str
    gpa: Optional[str] = None
    start_date: date
    end_date: Optional[date] = None
    graduated: bool


class Skill(ProfileBaseModel):
    id: int
    name: str
    category: Optional[str] = None
    strength: int
    years_experience: Optional[float] = None
    notes: Optional[str] = None


class WorkExperience(ProfileBaseModel):
    id: int
    company: str
    position: str
    location: Optional[str] = None
    employment_type: Optional[str] = None
    start_date: date
    end_date: Optional[date] = None
    currently_working: bool
    description: str


class Profile(ProfileBaseModel):
    awards: list[Award] = Field(default_factory=list)
    certifications: list[Certification] = Field(default_factory=list)
    educations: list[Education] = Field(default_factory=list)
    skills: list[Skill] = Field(default_factory=list)
    work_experiences: list[WorkExperience] = Field(default_factory=list)
