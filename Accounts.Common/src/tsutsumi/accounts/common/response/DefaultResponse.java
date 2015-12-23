package tsutsumi.accounts.common.response;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;

public class DefaultResponse extends Response {
	protected DocumentFragment getXmlContentFragment() {
		return null;
	}

	protected void populateContents(Node contents) {
	}
	
}
