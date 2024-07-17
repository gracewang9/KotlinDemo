import ktHttp.ApiService
import ktHttp.GitHubService
import ktHttp.KtHttpV1
import org.junit.jupiter.api.Test

class TestKtHttpV1 {
    @Test
    fun testKtHttpV1(){
        KtHttpV1.baseUrl="https://api.github.com"
        val api: GitHubService =KtHttpV1.create(ApiService::class.java)
        val data:User=api.search(id = "JetBrains")
    }
}