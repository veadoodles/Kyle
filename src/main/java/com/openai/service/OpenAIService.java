package com.openai.service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.AudioTranscription;
import com.azure.ai.openai.models.AudioTranscriptionFormat;
import com.azure.ai.openai.models.AudioTranscriptionOptions;
import com.azure.ai.openai.models.ChatChoice;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatMessage;
import com.azure.ai.openai.models.ChatRole;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;
import com.openai.model.AudioModel;

@Service
public class OpenAIService {

	public String runMinutes(String fileName) {
		String endValue = "";
		String emailFormattedValue = "";
		String summarizationValue = "";
		String conversationValue = "";

		// Audio Transcription
		AudioModel audio = new AudioModel();
		audio.setFileName(fileName);
		audio.setFilePath(Paths.get("C:\\Users\\daniel.k.penjan\\Downloads\\" + fileName));
		String transcription = runAudioTranscription(audio);

		//Conversation
		conversationValue = runSummarization(transcription, "Generate a full Conversation format from this value");
		//Summarization
		summarizationValue = runSummarization(conversationValue, "Generate a summary from this value ");
		//Email Formating
		emailFormattedValue = runSummarization(summarizationValue, "Generate an email format from this value and add action items from it");
		endValue = conversationValue + "\n";
		endValue += "---------------------------------- \n" ;
		endValue += summarizationValue + "\n";
		endValue += "---------------------------------- \n ";
		endValue += emailFormattedValue;

		return endValue;


	}

	public String runAudioTranscription(AudioModel model) {
		String transcribed = "";
		String azureOpenaiKey2 = "9a32c86a71804bef87bbf3beac024b50";
        String endpoint2 = "https://az-openai-audio-transcription.openai.azure.com/";
        String deploymentOrModelId = "whisper";

        OpenAIClient client2 = new OpenAIClientBuilder()
        		.endpoint(endpoint2)
        		.credential(new AzureKeyCredential(azureOpenaiKey2))
        		.buildClient();

        byte[] file = BinaryData.fromFile(model.getFilePath()).toBytes();
        AudioTranscriptionOptions transcriptionOptions = new AudioTranscriptionOptions(file)
        		.setResponseFormat(AudioTranscriptionFormat.JSON);

        System.out.println("Message: "+ transcriptionOptions.setPrompt("Extract Conversation from this file"));
        transcriptionOptions.setPrompt("Generate conversation from the file");
        String prmpt = transcriptionOptions.getPrompt();
        System.out.println("Message: " + prmpt);
        AudioTranscription transcription = client2.getAudioTranscription(deploymentOrModelId, model.getFileName(), transcriptionOptions);
        transcribed = transcription.getText();
        System.out.println("Transcription: " + transcription.getText());
        System.out.println("Value: " + transcribed);
        // TODO: Check if we can still do something in here.

        return transcribed;
	}

	public String runSummarization(String conversation, String prompt) {
		String summary = "";
        String azureOpenaiKey = "f37d36f9289843ae84d06b1c3b0bfde2";
        String endpoint = "https://myopenai-nullx.openai.azure.com/";
        OpenAIClient client = new OpenAIClientBuilder()
            .endpoint(endpoint)
            .credential(new AzureKeyCredential(azureOpenaiKey))
            .buildClient();

        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage(ChatRole.SYSTEM, "You are a helpful assistant"));
        chatMessages.add(new ChatMessage(ChatRole.USER, prompt + conversation
        		));

        // TODO: Update prompt to summarize

        ChatCompletions chatCompletions = client.getChatCompletions("AMT-2",
        	    new ChatCompletionsOptions(chatMessages));

        System.out.printf("Model ID=%s is created at %s.%n", chatCompletions.getId(), chatCompletions.getCreatedAt());
        for (ChatChoice choice : chatCompletions.getChoices()) {
            ChatMessage message = choice.getMessage();
            System.out.printf("Index: %d, Chat Role: %s.%n", choice.getIndex(), message.getRole());
            System.out.println("Message:");
            System.out.println(message.getContent());
            summary += message.getContent();
       }

        return summary;
	}
}
