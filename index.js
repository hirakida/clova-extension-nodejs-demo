const clova = require('@line/clova-cek-sdk-nodejs');
const express = require('express');
const APPLICATION_ID = process.env.APPLICATION_ID;

const clovaSkillHandler =
  clova.Client
       .configureSkill()
       .onLaunchRequest(responseHelper => {
         responseHelper.setSimpleSpeech(
           clova.SpeechBuilder.createSpeechText('起動しました')
         );
       })
       .onIntentRequest(async responseHelper => {
         const intent = responseHelper.getIntentName();
         switch (intent) {
           // Builtin
           case 'Clova.GuideIntent':
             responseHelper.setSimpleSpeech(
               clova.SpeechBuilder.createSpeechText('これはデモ用のスキルです')
             );
             break;
           case 'Clova.YesIntent':
             responseHelper.setSimpleSpeech(
               clova.SpeechBuilder.createSpeechText('はい')
             );
             break;
           case 'Clova.NoIntent':
             responseHelper.setSimpleSpeech(
               clova.SpeechBuilder.createSpeechText('いいえ')
             );
             break;
           case 'Clova.CancelIntent':
             responseHelper.setSimpleSpeech(
               clova.SpeechBuilder.createSpeechText('キャンセルします')
             );
             responseHelper.endSession();
             break;
         }
       })
       .onSessionEndedRequest(responseHelper => {
       })
       .handle();

const app = new express();
const clovaMiddleware = clova.Middleware({applicationId: APPLICATION_ID});
app.post('/', clovaMiddleware, clovaSkillHandler);

const port = process.env.PORT || 3000;
app.listen(port, () => {
  console.log(`Server running on ${port}`);
});
