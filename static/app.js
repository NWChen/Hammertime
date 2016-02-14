$.getJSON("hammertime.ngrok.com/get_data").then(populateCharts)

function populateCharts(data) {
  var data = data.data;
  var series = []
  var labels = []
  var alarmTimes = []
  var wakeUpTimes = []

  for (var i = 0; i < data.length; i++) {
    var alarmTime = data[i].alarm_time["$date"];
    var wakeUpTime = data[i].wake_up_time["$date"];
    alarmTimes.push(alarmTime);
    wakeUpTimes.push(wakeUpTime);
    var now = moment(alarmTime).local(); 
    var then = moment(wakeUpTime).local();
    var minutes = moment.duration(then.diff(now)).minutes();
    // console.log(minutes);
    series.push(minutes);
    var label = now.format("MMM D");
    // console.log(label);
    labels.push(label);
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