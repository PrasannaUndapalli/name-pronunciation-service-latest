package com.wf.nps.ml;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.cognitiveservices.speech.AutoDetectSourceLanguageConfig;
import com.microsoft.cognitiveservices.speech.CancellationReason;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisCancellationDetails;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;
import com.microsoft.cognitiveservices.speech.SynthesisVoicesResult;
import com.microsoft.cognitiveservices.speech.VoiceInfo;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

import io.micrometer.core.instrument.util.StringUtils;

public class SpeechSynthesis {

	private static Logger logger = LoggerFactory.getLogger(SpeechSynthesis.class);
	
	private String subscriptionKey = "21d31f16a59645c9ade00b723ede92d7";
	private String serviceRegion = "eastus";

	// Speech synthesis get available voices
	public List<VoiceInfo> getAvailableVoicesAsync(String localeText)
			throws InterruptedException, ExecutionException {
		List<VoiceInfo> voiceList = null;

		// Creates an instance of a speech config with specified
		// subscription key and service region.
		SpeechConfig speechConfig = SpeechConfig.fromSubscription(subscriptionKey, serviceRegion);

		// Creates a speech synthesizer
		SpeechSynthesizer synthesizer = new SpeechSynthesizer(speechConfig, null);

		SynthesisVoicesResult result = synthesizer.getVoicesAsync(localeText).get();

		// Checks result.
		if (result.getReason() == ResultReason.VoicesListRetrieved) {
			logger.info("Voices successfully retrieved, they are:");
			voiceList = result.getVoices();
			for (VoiceInfo voice : result.getVoices()) {
				logger.info(voice.getName());
			}
		} else if (result.getReason() == ResultReason.Canceled) {
			logger.info("CANCELED: ErrorDetails=" + result.getErrorDetails());
			logger.info("CANCELED: Did you update the subscription info?");
		}

		result.close();
		synthesizer.close();

		return voiceList;
	}

	// Gets synthesized audio data from result.
	public byte[] synthesisToResultAsync(String text, String prefLanguage, String prefVoice,
			String prefSpeakingSpeed) throws InterruptedException, ExecutionException {
		byte[] audioData = null;
		// Creates an instance of a speech config with specified
		// subscription key and service region.
		SpeechConfig config = SpeechConfig.fromSubscription(subscriptionKey, serviceRegion);

		SpeechSynthesizer synthesizer;
		SpeechSynthesisResult result;

		if (StringUtils.isNotBlank(prefLanguage) && StringUtils.isNotBlank(prefVoice)) {
			// Creates a speech synthesizer with a null output stream.
			// This means the audio output data will not be written to any stream.
			// You can just get the audio from the result.
			synthesizer = new SpeechSynthesizer(config);
			if (StringUtils.isBlank(prefSpeakingSpeed)) {
				prefSpeakingSpeed = "0";
			}
			String ssml = "<speak xmlns='http://www.w3.org/2001/10/synthesis' xmlns:mstts='http://www.w3.org/2001/mstts' xmlns:emo='http://www.w3.org/2009/10/emotionml' version='1.0' xml:lang='en-US'><voice name='"
					+ prefLanguage + "-" + prefVoice + "'><prosody rate='" + prefSpeakingSpeed + "%'>" + text
					+ "</prosody></voice></speak>";
			logger.info("synthesisToResultAsync using SSML:" + ssml);
			result = synthesizer.SpeakSsmlAsync(ssml).get();
		} else {
			AutoDetectSourceLanguageConfig autoDetectSourceLanguageConfig = AutoDetectSourceLanguageConfig
					.fromOpenRange();
			// Creates a speech synthesizer with auto-detection for source language
			synthesizer = new SpeechSynthesizer(config, autoDetectSourceLanguageConfig,
					AudioConfig.fromDefaultSpeakerOutput());
			logger.info("synthesisToResultAsync using AutoDetectSourceLanguage");
			result = synthesizer.SpeakTextAsync(text).get();
		}
		// Checks result.
		if (result.getReason() == ResultReason.SynthesizingAudioCompleted) {
			audioData = result.getAudioData();
			logger.info("Speech synthesized to speaker for text [" + text + "]");
			logger.info(audioData.length + " bytes of audio data received for text [" + text + "]");
		} else if (result.getReason() == ResultReason.Canceled) {
			SpeechSynthesisCancellationDetails cancellation = SpeechSynthesisCancellationDetails.fromResult(result);
			logger.info("CANCELED: Reason=" + cancellation.getReason());

			if (cancellation.getReason() == CancellationReason.Error) {
				logger.info("CANCELED: ErrorCode=" + cancellation.getErrorCode());
				logger.info("CANCELED: ErrorDetails=" + cancellation.getErrorDetails());
				logger.info("CANCELED: Did you update the subscription info?");
			}
		}

		result.close();
		synthesizer.close();

		return audioData;
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		SpeechSynthesis ss = new SpeechSynthesis();
		System.out.println(ss.synthesisToResultAsync("geetha reddy","en-IN","PrabhatNeural",""));
	}
}
