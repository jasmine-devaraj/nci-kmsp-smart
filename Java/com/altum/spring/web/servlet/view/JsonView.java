package com.altum.spring.web.servlet.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;
import com.altum.spring.web.util.JsonSerializerSupport;

public class JsonView extends AbstractView {
	//private JSONSerializer jsonSerializer;
	private JsonSerializerSupport jsonSupport;

	/**
	 * Loop through all objects in the model and create json strings from them. 
	 */
	@Override
	protected void renderMergedOutputModel(Map model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StringBuffer json = new StringBuffer();
		if (model.size() == 1) {
			json.append(jsonSupport.toJSON(model.values().iterator().next()));
		}else{
			json.append(jsonSupport.toJSON(model));
		}
		response.getWriter().print(json);
	}

	public void setJsonSerializer(JsonSerializerSupport jsonSupport){
		this.jsonSupport = jsonSupport;
	}
}
