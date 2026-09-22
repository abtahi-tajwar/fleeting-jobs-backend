from pydantic import BaseModel, ConfigDict

class PydanticCamelCaseModel(BaseModel):
    model_config = ConfigDict(
        alias_generator=lambda field: ''.join(
            word.capitalize() if i else word
            for i, word in enumerate(field.split('_'))
        ),
        populate_by_name=True
    )