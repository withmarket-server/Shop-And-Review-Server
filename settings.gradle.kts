rootProject.name = "yumarket"

include(
    ":application-query",
    ":application-command",
    ":application-kafka",
    ":domain-dynamo",
    ":domain-shopquery",
    ":domain-shopcommand",
    ":domain-kafka",
    ":common",
    ":client-query",
    ":client-command",
    ":infrastructure"
)