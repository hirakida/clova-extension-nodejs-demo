import os
import sys

import cek
import requests
from flask import Flask, request, jsonify

app = Flask(__name__)

application_id = os.getenv("APPLICATION_ID", None)
if application_id is None:
    app.logger.error("APPLICATION_ID not found.")
    sys.exit(1)

clova = cek.Clova(application_id=application_id, default_language="ja", debug_mode=False)


@app.route("/", methods=["POST"])
def my_service():
    body_dict = clova.route(body=request.data, header=request.headers)
    response = jsonify(body_dict)
    response.headers["Content-Type"] = "application/json;charset-UTF-8"
    return response


@clova.handle.launch
def launch_handler():
    return clova.response("起動しました")


@clova.handle.intent("Clova.GuideIntent")
def guide_intent_handler(clova_request):
    return clova.response("１ビットコインいくらと話しかけてください")


@clova.handle.intent("BTC_JPY")
def intent_handler(clova_request):
    number = clova_request.slot_value("number")
    if number is None:
        return clova.response("もう一度、お願いします")

    sell = get_price(number=number, order_type="sell")
    buy = get_price(number=number, order_type="buy")
    if sell is None or buy is None:
        return clova.response("エラーが発生しました")
    message = "売値は{}円、買値は{}円です".format(sell, buy)
    return clova.response(message)


@clova.handle.default
def default_handler():
    return clova.response("もう一度、お願いします")


def get_price(number, order_type):
    url = "https://coincheck.com/api/exchange/orders/rate"
    params = {"pair": "btc_jpy", "amount": number, "order_type": order_type}
    try:
        response = requests.get(url, params=params)
        content = response.json()
        return round(float(content["price"]))
    except Exception as err:
        app.logger.error(err)
        return None


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080, debug=False)
