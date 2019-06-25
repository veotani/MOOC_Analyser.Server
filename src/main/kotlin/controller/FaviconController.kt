package web.api.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.GetMapping


@Controller
internal class FaviconController {

    @GetMapping("/favicon.ico")
    @ResponseBody
    fun returnNoFavicon() {
    }
}
