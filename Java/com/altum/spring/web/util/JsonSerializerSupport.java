package com.altum.spring.web.util;

import java.util.List;

import com.metaparadigm.jsonrpc.JSONSerializer;
import com.metaparadigm.jsonrpc.Serializer;

/**
 * Decorator class for use in Spring config
 * @author luke.dion
 *
 */
public class JsonSerializerSupport {
	private JSONSerializer serializer = new JSONSerializer();

	public JsonSerializerSupport(){
		serializer = new JSONSerializer();
        try {
        	serializer.registerDefaultSerializers();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	public void setCustomSerializers(List<Serializer> serializers){
		try{
			for (Serializer custom : serializers){
				serializer.registerSerializer(custom);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Object fromJSON(String jsonText){
		try{
			return this.serializer.fromJSON(jsonText);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	public String toJSON(Object o){
		try{
			return this.serializer.toJSON(o);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}

