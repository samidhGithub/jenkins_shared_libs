# vars/listPullRequests.groovy

def call(String repoOwner, String repoSlug, String bitbucketUsername, String bitbucketAppPassword) {
    def auth = "${bitbucketUsername}:${bitbucketAppPassword}".bytes.encodeBase64().toString()
    def apiUrl = "https://api.bitbucket.org/2.0/repositories/${repoOwner}/${repoSlug}/pullrequests"

    def response = httpRequest(
        acceptType: 'APPLICATION_JSON',
        authentication: auth,
        httpMode: 'GET',
        responseHandle: 'NONE',
        url: apiUrl
    )

    if (response.getStatus() == 200) {
        def pullRequests = readJSON text: response.getContent()
        return pullRequests.values.collect { pullRequest ->
            return "Pull Request: ${pullRequest.title}, ID: ${pullRequest.id}"
        }
    } else {
        error "Failed to fetch pull requests. HTTP Status: ${response.getStatus()}"
    }
}
