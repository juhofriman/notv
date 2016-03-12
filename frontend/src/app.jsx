/** @jsx React.DOM */
'use strict'

// Require sass files, webpack injects them to site
require("../styles/notv.scss")

var React = require('react')
var ReactDOM = require('react-dom')
var update = require('react-addons-update');
var classNames = require('classnames');
var axios = require('axios');


var NoTV = React.createClass({
  getInitialState: function() {
    return {
      channels: []
    };
  },
  componentDidMount: function() {
    axios.get('/api/channels')
      .then(function (response) {
        this.setState(update(this.state, { 'channels': {'$push': response.data }}))
      }.bind(this));

  },
  render: function() {
    return (
      <div className="notv-container">
        <div className="notv-header">
          <div className="logo">
            <h4>Se parempi televisio-opas</h4>
          </div>
        </div>
        <div className="notv-body">
          {this.state.channels.map(function(channel, i){
            return (
              <div key={i}>{channel.name}</div>
            );
          })}
        </div>
      </div>
    );
  }
});

ReactDOM.render(
  <NoTV/>,
  document.getElementById('notv-react')
);
