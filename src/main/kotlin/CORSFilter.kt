package web.api

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.web.filter.OncePerRequestFilter

class CORSFilter : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        println("FILTER")
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200")
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        response.setHeader(
            "Access-Control-Allow-Headers",
            "Origin, Accept, X-Requested-With, " +
                    "Content-Type, Access-Control-Request-Method, " +
                    "Access-Control-Request-Headers, " +
                    "User-Agent, Referer, Content-Length, Content-Language, " +
                    "Date, Authorization"
        )
        chain.doFilter(request, response)
    }
}
