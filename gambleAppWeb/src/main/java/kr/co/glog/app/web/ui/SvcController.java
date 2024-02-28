package kr.co.glog.app.web.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Iterator;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class SvcController {

	private final String _ROOT = "web/svc";


	@GetMapping("/svc/{p1}/{p2}")
	public String p1p2(Model model, @PathVariable("p1") String p1, @PathVariable("p2") String p2, @RequestParam Map<String,Object> serverParam ) {

		model.addAttribute("serverParam", serverParam );

		String url = _ROOT;
		if ( StringUtils.hasText( p1 ) )  url += "/" + p1;
		if ( StringUtils.hasText( p2 ) )  url += "/" + p2;


		return url;
	}


	@GetMapping("/svc/{p1}/{p2}/{p3}")
	public String svcp1p2p3(Model model, @PathVariable("p1") String p1, @PathVariable("p2") String p2,  @PathVariable("p3") String p3, @RequestParam Map<String,Object> serverParam ) throws JsonProcessingException {

		// URL
		String url = _ROOT;
		if ( StringUtils.hasText( p1 ) )  url += "/" + p1;
		if ( StringUtils.hasText( p2 ) )  url += "/" + p2;
		if ( StringUtils.hasText( p3 ) )  url += "/" + p3;

		// PARAMETER
		model.addAttribute("serverParam", serverParam );

		return url;
	}

	@GetMapping("/svc/{p1}/{p2}/{p3}/{p4}")
	public String svcp1p2p3p4(Model model, @PathVariable("p1") String p1, @PathVariable("p2") String p2,  @PathVariable("p3") String p3, @PathVariable("p4") String p4, @RequestParam Map<String,Object> serverParam ) throws JsonProcessingException {

		// URL
		String url = _ROOT;
		if ( StringUtils.hasText( p1 ) )  url += "/" + p1;
		if ( StringUtils.hasText( p2 ) )  url += "/" + p2;
		if ( StringUtils.hasText( p3 ) )  url += "/" + p3;
		if ( StringUtils.hasText( p4 ) )  url += "/" + p4;

		// PARAMETER
		model.addAttribute("serverParam", serverParam );

		return url;
	}

	public static void main(String[] args) {
		String encode = new BCryptPasswordEncoder().encode("123456789");
		System.out.println("encode = " + encode);
	}

}