#define CW 10
#define CCW 11
#define POT 0
#define HIT_THRESHOLD 600
#define REWIND_THRESHOLD 300

unsigned long prevMillis = 0;
unsigned long currentMillis = millis();
unsigned long prevMillis2 = 0;
unsigned long currentMillis2 = millis();

void setup() { 
  Serial.begin(9600);
  pinMode(CW, OUTPUT); 
  pinMode(CCW, OUTPUT); 
}

void loop() { 
  if(Serial.available()) {
    if(Serial.read()=='1')
      hammertime();
    else stop_hammer();
  }
}

void hammertime() {
  Serial.println("Stop! Hammer time!");
  currentMillis = millis();
  currentMillis2 = millis();
  if(currentMillis - prevMillis >= 500) {
    hit();
    prevMillis = currentMillis;
  }
  if(currentMillis2 - prevMillis2 >= 100) {
    rewind();
    prevMillis2 = currentMillis2;
  }
}

void hit() {
  while(read_pot()<HIT_THRESHOLD) {
    //Serial.println("CCW");
    //Serial.println(analogRead(POT));
    digitalWrite(CCW, HIGH);
  }
  digitalWrite(CCW, LOW);
}

void rewind() {
  while(read_pot()>REWIND_THRESHOLD) {
    //Serial.println("CW");
    //Serial.println(analogRead(POT));
    digitalWrite(CW, HIGH);
  }
  digitalWrite(CW, LOW);
}

void stop_hammer() {
  Serial.println("Hammer time ceased.");
  digitalWrite(CW, LOW);
  digitalWrite(CCW, LOW);
}

int read_pot() {
  return analogRead(POT);
}

