package main

import (
	"encoding/json"
	"log"
	"net/http"
	"os"

	"github.com/line/clova-cek-sdk-go/cek"
)

func main() {
	ext := cek.NewExtension(os.Getenv("APPLICATION_ID"))

	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		message, err := ext.ParseRequest(r)
		if err != nil {
			log.Printf("Invalid request")
			http.Error(w, http.StatusText(http.StatusBadRequest), http.StatusBadRequest)
			return
		}

		var response *cek.ResponseMessage
		switch request := message.Request.(type) {
		case *cek.LaunchRequest:
			response = createResponse("起動しました", false)
		case *cek.IntentRequest:
			switch request.Intent.Name {
			case "Clova.GuideIntent":
				response = createResponse("これはデモ用のスキルです", false)
			case "Clova.YesIntent":
				response = createResponse("はい", false)
			case "Clova.NoIntent":
				response = createResponse("いいえ", false)
			case "Clova.CancelIntent":
				response = createResponse("キャンセルします", true)
			case "Clova.FallbackIntent":
				response = createResponse("予想していない発話です", false)
			default:
				response = createResponse("もう一度、お願いします", false)
			}
		}

		if response != nil {
			if err := json.NewEncoder(w).Encode(response); err != nil {
				http.Error(w, http.StatusText(http.StatusInternalServerError), http.StatusInternalServerError)
				return
			}
		}
	})

	if err := http.ListenAndServe(":"+os.Getenv("PORT"), nil); err != nil {
		log.Fatal(err)
	}
}

func createResponse(speech string, endSession bool) *cek.ResponseMessage {
	return cek.NewResponseBuilder().
		OutputSpeech(
			cek.NewOutputSpeechBuilder().
				AddSpeechText(speech, cek.SpeechInfoLangJA).Build()).
		ShouldEndSession(endSession).
		Build()
}
