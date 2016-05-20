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
request: null,
getInitialState: function()
{
  return {"data": null};
},
componentDidMount: function()
{
  this.request = $.get("./api.json", function(data)
  {
    this.setState({"data": data});
  }.bind(this));
},
componentWillUnmount: function()
{
  if(this.request) this.request.abort();
},
render: function()
{
  return <pre>{JSON.stringify(this.state.data)}</pre>
}
			});
			
			ReactDOM.render(
		<ComponentList color="#aaa"/>,
				document.getElementById('componentList')
			);
		// ]]>	 
		</script>
	</body>
</html>