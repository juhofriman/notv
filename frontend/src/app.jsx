/** @jsx React.DOM */
'use strict'

// Require sass files, webpack injects them to site
require("../styles/notv.scss")

var React = require('react')
var ReactDOM = require('react-dom')
var update = require('react-addons-update');
var classNames = require('classnames');

var notvSearch = {
  programMatches: function(q, program) {
    if(q.trim() === '') { return false; }
    return program.name.toUpperCase().indexOf(q.toUpperCase()) > -1;
  },
  channelMatches: function(q, programs) {
    if(q.trim() === '') { return false; }
    return programs.some(function(p) {
      return this.programMatches(q, p);
    }, this);
  }
};

var Program = React.createClass({
  render: function() {
    var programHeigh = { height: this.props.program.length };
    var classes = classNames('program', {
        'matches': this.props.program.matches
    });
    return (
        <div className={classes} style={programHeigh}>{this.props.program.name}</div>
    );
  }
});

var Schedule = React.createClass({
  render: function() {
    return (
      <div className="schedule-container">
        <div className="schedule">
          {this.props.channel.name}
          {this.props.channel.programs.map(function(program, i){
            return <Program program={program} key={i} />;
          })}
        </div>
      </div>
    );
  }
});

var Timeline = React.createClass({
  getTimelineStyling: function() {
    // Here we should get current time and scale by that
    return {
      pastStyle: { height: 300 },
      futureStyle: { height: 100 }
    };
  },
  render: function() {
    var timelineStyling = this.getTimelineStyling();
    return (
      <div className="timeline">
        <div className="block hours">
          <div className="point">13:00</div>
          <div className="point">14:00</div>
          <div className="point">15:00</div>
          <div className="point">16:00</div>
          <div className="point">17:00</div>
        </div>
        <div className="block">
          {this.props.channels.map(function(channel, i){
            return <Schedule channel={channel} key={i} />;
          })}
          <div className="past" style={timelineStyling.pastStyle}></div>
          <div className="now-cue"></div>
          <div className="future" style={timelineStyling.futureStyle}></div>

        </div>
      </div>
    );
  }
});

var ChannelToggle = React.createClass({
  toggle: function(e) {
    this.props.toggleCallback(this.props.channel);
  },
  render: function() {
    var classes = classNames('channel-toggle', {
        'toggled': this.props.channel.toggled,
        'matches': this.props.channel.matches
    });
    return (
      <div className={classes}>
        <div onClick={this.toggle} className="name">{this.props.channel.name}</div>
      </div>
    );
  }
});

var NoTVSearch = React.createClass({
  change: function(e) {
    if(e.target.value.length > 2) {
      this.props.changeQueryCallback(e.target.value);
    } else {
      this.props.changeQueryCallback('');
    }
  },
  render: function() {
    return (
      <div className="notv-search">
        <input onChange={this.change} type="text"/>
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
        {'name': 'Kanava A',
         'toggled': false,
         'programs': [
           { length: 20, name: 'Uutiset'},
           { length: 100, name: 'Haluatko miljonääriksi?'},
           { length: 50, name: 'Reinikainen'},
           { length: 30, name: 'Tankki täyteen'},
           { length: 60, name: 'Rekkakuskit lesboina'},
           { length: 200, name: 'Eduskunnan täysistunto'}
         ]},
         {'name': 'Kanava B',
          'toggled': false,
          'programs': [
            { length: 100, name: 'James Potkukelkka -show'},
            { length: 150, name: 'Hullut siivoojat'},
            { length: 50, name: 'Kauniit ja rohkeat'},
            { length: 10, name: 'Mainos-tv'},
            { length: 80, name: 'Pahkasika'},
            { length: 50, name: 'Jari Sarasvuo'}
          ]},
          {'name': 'Kanava C',
           'toggled': false,
           'programs': [
             { length: 100, name: 'Hevoshullu'},
             { length: 300, name: 'Olympialaiset'},
             { length: 50, name: 'Elokuva: Keisarikunta'},
             { length: 10, name: 'Kotkalaisen lauluperinteen perässä'},
             { length: 80, name: 'Jeppistä'},
             { length: 10, name: 'Keno!'}
           ]},
           {'name': 'Kanava D',
            'toggled': false,
            'programs': [
              { length: 100, name: 'Ryhmä-X'},
              { length: 150, name: 'Mönkiäisohjelmaa'},
              { length: 50, name: 'Venäläiset suolakurkut'},
              { length: 10, name: 'Uutiset'}
            ]},
       ],
     order: [],
     query: ''};
  },
  toggleChannel: function(channel) {
    this.setState(
      update(this.state,
        {'channels':
          { '$apply': function(ov) {
            return ov.map(function(c) {
              if(c.name === channel.name) {
                return { 'name': c.name, 'toggled': !c.toggled, 'programs': c.programs };
              } else {
                return c;
              }
            });
          }},
        'order': { '$apply': function(oldOrder) {
          var index = oldOrder.indexOf(channel.name);
          if(index === -1) {
            oldOrder.push(channel.name);
          } else {
            oldOrder.splice(index, 1);
          }
          return oldOrder;
        }}}));
  },
  changeQueryState: function(query) {
    this.setState(update(this.state, {'query': {'$set': query}}));
  },
  getProgramArrays: function() {
    // Horrible, but catches the idea...
    var a = this.state.order.map(function(cName) {
      return this.state.channels.filter(function(c) { return c.name === cName})[0]
    }, this);
    return a.map(function(channel) {
      channel['programs'] = channel.programs.map(function(program) {
        program['matches'] = notvSearch.programMatches(this.state.query, program);
        return program;
      }, this);
      return channel;
    }, this);
  },
  getChannelsWithMatchesMarked: function() {
    // Horrible, but catches the idea...
    return this.state.channels.map(function(channel) {
      channel['matches'] = notvSearch.channelMatches(this.state.query, channel.programs);
      return channel;
    }, this);
  },
  render: function() {
    return (
      <div className="notv-container">
        <div>
          <NoTVSearch changeQueryCallback={this.changeQueryState}/>
        </div>
        <div>
          <div className="channel-listing-float">
            <ChannelSelector channels={this.getChannelsWithMatchesMarked()} toggleCallback={this.toggleChannel}/>
          </div>
          <div className="timeline-float">
            <Timeline channels={this.getProgramArrays()}/>
          </div>
        </div>
      </div>
    );
  }
});

ReactDOM.render(
  <NoTV/>,
  document.getElementById('notv-react')
);
