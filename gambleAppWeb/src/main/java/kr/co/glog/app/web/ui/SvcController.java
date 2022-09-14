package kr.co.glog.app.web.ui;

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
public class SvcController {

	private final String _ROOT = "web/svc";


	@GetMapping("/svc/{p1}/{p2}")
	public String p1p2(Model model, @PathVariable("p1") String p1, @PathVariable("p2") String p2 ) {
		model.addAttribute( "greetings", "hi! everyone");
		String url = _ROOT;
		if ( StringUtils.hasText( p1 ) )  url += "/" + p1;
		if ( StringUtils.hasText( p2 ) )  url += "/" + p2;
		return url;
	}


	@GetMapping("/svc/{p1}/{p2}/{p3}")
	public String svcp1p2p3(Model model, @PathVariable("p1") String p1, @PathVariable("p2") String p2,  @PathVariable("p3") String p3 ) {
		model.addAttribute( "greetings", "hi! everyone");
		String url = _ROOT;
		if ( StringUtils.hasText( p1 ) )  url += "/" + p1;
		if ( StringUtils.hasText( p2 ) )  url += "/" + p2;
		if ( StringUtils.hasText( p3 ) )  url += "/" + p3;
		return url;
	}



}