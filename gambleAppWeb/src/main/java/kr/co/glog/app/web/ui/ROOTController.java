package kr.co.glog.app.web.ui;

import kr.co.glog.AppConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class ROOTController {

	private final AppConfig appConfig;
	private final String _ROOT = "web";

	@GetMapping("/")
	public String index( Model model ) {
		model.addAttribute( "greetings", "hi! everyone");
		commonAddAttribute(model);
		return _ROOT + "/index";
	}

	@GetMapping("/{p1}/{p2}")
	public String p1p2(Model model, @PathVariable("p1") String p1, @PathVariable("p2") String p2 ) {
		model.addAttribute( "greetings", "hi! everyone");
		String url = _ROOT;
		if ( StringUtils.hasText( p1 ) )  url += "/" + p1;
		if ( StringUtils.hasText( p2 ) )  url += "/" + p2;

		commonAddAttribute(model);
		return url;
	}

	/**
	 * view 단으로 넘겨 줄 어플리케이션 공통 변수
	 */
	private Model commonAddAttribute(Model model) {
		model.addAttribute("appConfig", appConfig);
		return model;
	}
}