/** @jsx React.DOM */
'use strict'

// Require sass files, webpack injects them to site
require("../styles/notv.scss")

var React = require('react')
var ReactDOM = require('react-dom')

var ChannelSelector = React.createClass({
  render: function() {
    return(
      <div>
        <h2>Valitse kanavat</h2>
      </div>
    );
  }
});

var NoTV = React.createClass({
  render: function() {
    return (
      <div className="notv-container">
        <ChannelSelector></ChannelSelector>
      </div>
    );
  }
});

ReactDOM.render(
  <NoTV/>,
  document.getElementById('notv-react')
);
