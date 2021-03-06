package sx.blah.discord.json.responses.events;

/**
 * This response is received when a message is deleted
 */
public class MessageDeleteEventResponse {

	/**
	 * The message id
	 */
	public String id;

	/**
	 * The channel the message was deleted from
	 */
	public String channel_id;

	public MessageDeleteEventResponse() {

	}

	public MessageDeleteEventResponse(String id, String channel_id) {
		this.id = id;
		this.channel_id = channel_id;
	}
}
