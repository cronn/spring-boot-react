'use strict';

var ReactTransitionGroup = React.addons.TransitionGroup;

var SlideAnimationMixin = {
	componentWillEnter: function(callback)
	{
    	$(ReactDOM.findDOMNode(this)).hide();
        $(ReactDOM.findDOMNode(this)).slideDown(function(){
            callback();
        });
    },
    componentWillAppear: function(callback){
        callback();
    },
    componentWillLeave: function(callback){
    	$(ReactDOM.findDOMNode(this)).slideUp(function(){
            callback();
        });
    }
};

var AjaxMixin = {
	queries: {},

	loadAjax: function(request, stateName)
	{
		this.queries[stateName] = request;
		request.done(function(data) {
			var newState = {};
			newState[stateName] = data;
			this.setState(newState);
		}.bind(this));
	},

	componentWillUnmount: function()
	 {
		for(var key in queries)
		{
			queries[key].abort();
		}
	}
}

var ClosablePanel = React.createClass({
	mixins: [SlideAnimationMixin],

	getInitialState: function ()
	{
		return {"closed": this.props.initialClosed};
	},
	toggleClosed: function ()
	{
		var newState = !this.state.closed;
		if(this.props.onStateChange)
		{
			this.props.onStateChange(newState);
		}
		this.setState({"closed": newState});
	},
	render: function ()
	{
		var body = null;
		if (!this.state.closed)
		{
			body = <div className="panel-body">
				{this.props.children}
			</div>;
		}
		return <div className="panel panel-warning">
			<div className="panel-heading" onClick={this.toggleClosed}>
			  	<span className="pull-right">
			  		<span className={"glyphicon glyphicon-" + (this.state.closed ? "menu-right" : "menu-down")}></span>
			  	</span>
		  		{this.props.title}
			</div>
			{body}
		</div>;
	}
});

var ComponentList = React.createClass({
	request: null,

	componentDidMount: function()
	{
	    this.reloadNewestLogs();

	    this.timer = setInterval(this.reloadNewestLogs, 1000);
	},

	panelsOpen: 0,

	onPanelStateChange: function(closed)
	{
		if(closed) {
			this.panelsOpen --;
		}
		else {
			this.panelsOpen ++;
		}
	},

	reloadNewestLogs: function()
	{
		if(this.panelsOpen == 0)
		{
			this.request = $.get("/logs/newest").done(function(data)
			{
				if(this.panelsOpen == 0) {
					this.setState({"newestLogs": data});
				}
		    }.bind(this));
		}
	},

  componentWillUnmount: function()
  {
      clearInterval(this.timer);
      if(request) request.abort();
  },

  getInitialState: function()
  {
  	return {"newestLogs": null};
  },

	render: function()
	{
		if(this.state.newestLogs)
		{
			var newestLogs = this.state.newestLogs.map(function(log)
			{
				return  <ClosablePanel
					 		key={log.id}
							title={log.title}
							initialClosed={true}
							onStateChange={this.onPanelStateChange}>
						{log.message}
					</ClosablePanel>
				;
			}.bind(this));
			return <ReactTransitionGroup>{newestLogs}</ReactTransitionGroup>;
		}
		else
		{
			return <span>Loading</span>;
		}
	}
});

ReactDOM.render(
	<ComponentList />,
	document.getElementById('componentList')
);
