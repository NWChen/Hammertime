$.jsonp("localhost:5000/home").then(populateCharts(data))

function populateCharts(data) {
  var data = [1,2,3,4,5,6,7]
  var series = []
  var labels = []
  for (var i = 0; i < data.length; i++) {
    // data[i].alarmTime
    // data[i].wakeUpTime
    var now = moment().local(); // alarmTime
    var then = moment().local().add(i, 'minutes'); // wakeUpTime
    var minutes = moment.duration(then.diff(now)).minutes()
    series.push(minutes)
    labels.push(now.month() + "/" + now.day())
  };

  var data = {
    labels: labels,
    series: [series]
  };

  var options = {
    axisY: {
      onlyInteger: true,
      offset: 50,
      labelInterpolationFnc: function(value) {
        return value + ' min'
      },
      scaleMinSpace: 15
    }

  };

  new Chartist.Bar('#chart1', data, options);

  var data = [1,2,3,4,5,6,7]
  var alarmTimes = []
  var wakeUpTimes = []

  for (var i = 0; i < data.length; i++) {
    // data[i].alarmTime
    // data[i].wakeUpTime
    var now = moment().local(); 
    var then = moment().local().add(i, 'minutes'); 
    
    alarmTimes.push(now.toDate().getTime());
    wakeUpTimes.push(then.toDate().getTime());
  };

  var data = {
    labels: labels,
    series: [alarmTimes, wakeUpTimes]
  };

  var options = {
    showLine: false,
    axisY: {
      offset: 50,
      labelInterpolationFnc: function(value) {
        return moment(value).format("h:mm:ss")
      }
    }
  };

  new Chartist.Line('#chart2', data, options);
}