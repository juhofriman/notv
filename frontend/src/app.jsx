/** @jsx React.DOM */
'use strict'

// Require sass files, webpack injects them to site
require("../styles/notv.scss")

var React = require('react')
var ReactDOM = require('react-dom')
var update = require('react-addons-update');
var classNames = require('classnames');

var ChannelToggle = React.createClass({
  toggle: function(e) {
    this.props.toggleCallback(this.props.channel);
  },
  render: function() {
    var classes = classNames('channel-toggle', {
        'toggled': this.props.channel.toggled
    });
    return (
      <div className={classes}>
        <div onClick={this.toggle} className="name">{this.props.channel.name}</div>
      </div>
    );
  }
});

var ChannelSelector = React.createClass({
  render: function() {
    var toggleCallback = this.props.toggleCallback;
    return(
      <div>
        <div className="channel-listing">
          {this.props.channels.map(function(channel, i){
            return <ChannelToggle channel={channel} toggleCallback={toggleCallback} key={i} />;
          })}
        </div>
      </div>
    );
  }
});

var NoTV = React.createClass({
  getInitialState: function() {
    return {
      channels: [
        {'name': 'Kanava A', 'toggled': false},
        {'name': 'Kanava B', 'toggled': false},
        {'name': 'Kanava C', 'toggled': false},
        {'name': 'Kanava D', 'toggled': false}]};
  },
  toggleChannel: function(channel) {
    this.setState(
      update(this.state,
        {'channels':
          { '$apply': function(ov) {
            return ov.map(function(c) {
              if(c.name === channel.name) {
                return { 'name': c.name, 'toggled': !c.toggled };
              } else {
                return c;
              }
            });
          }}}));
  },
  render: function() {
    return (
      <div className="notv-container">
        <div>
          <input type="text"/>
        </div>
        <div>
          <ChannelSelector channels={this.state.channels} toggleCallback={this.toggleChannel}/>
        </div>
      </div>
    );
  }
});

ReactDOM.render(
  <NoTV/>,
  document.getElementById('notv-react')
);
