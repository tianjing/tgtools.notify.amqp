function NotifyWebSocket(option) {
    this._option = option;
    var _isOpen=false;
    var that = this;
    this.isOpen=function()
    {
        return _isOpen;
    }
    this.send = function (data) {
        that._wstext.send(data)
    };
    this._wstext = new WebSocket(this._option.url);
    this._wstext.onopen = function () {
        console.log('onopen');
        _isOpen=true;
        if (that._option.onopen) {
            that._option.onopen();
        }
    };
    this._wstext.onerror = function (e) {
        console.log('onerror');
        if (that._option.onclose) {
            that._option.onclose(e);
        }
    }
    this._wstext.onmessage = function (e) {
        console.log("onmessage");
        var data = eval("(" + e.data + ")");
        if (that._option.callback[data.type]) {
            that._option.callback[data.type](data.content);
        }
    };

    this._wstext.onclose = function (a, b) {
        _isOpen=false;
        console.log('onclose');
        if (that._option.onclose) {
            that._option.onclose(a, b);
        }
    };
};