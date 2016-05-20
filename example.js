<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title>ReactJS Demo</title>
				<link href="css/bootstrap.min.css" rel="stylesheet" />
				<script src="js/jquery.min.js" type="text/javascript"></script>
				<script src="js/react-with-addons.js"></script>
				<script src="js/react-dom.js"></script>
				<script src="js/browser.min.js"></script> 
				<script src="js/bootstrap.min.js" type="text/javascript"></script>
	</head>
 
	<body>
		<div class="container">
			<div class="panel panel-primary">
				<div class="panel-heading">Timer</div>
				<div class="panel-body" id="componentList">Loading ...</div>
			</div>
		</div>

		<script type="text/babel">
		// <![CDATA[
			
			var ComponentList = React.createClass({
				render: function() {
					return <div>
						<p>{this.state.time.toString()}</p>
						<p>{this.state.counter}</p>
					</div>;
				},
				componentDidMount: function() {
					this.timer = setInterval(this.updateTime, 1000);
				},
				componentWillUnmount: function() {
					clearTimeout(this.timer);
				},
				updateTime: function() {
					this.setState({"time": new Date(), "counter": this.state.counter + 1});
				},
				
				getInitialState: function() {
					return {"time": new Date(), "counter": 0};
				}
				
			});
			
			ReactDOM.render(
		<ComponentList />,
				document.getElementById('componentList')
			);
		// ]]>	 
		</script>
	</body>
</html>