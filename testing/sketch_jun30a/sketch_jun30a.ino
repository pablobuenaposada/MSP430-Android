int led = 13;
char incomingByte = '3';	// para el byte leido

void setup(){
  pinMode(led,OUTPUT);
  Serial.begin(9600);
}

void loop(){
  if (Serial.available() > 0) {
    // lee el byte entrante:
    incomingByte = Serial.read();

    if (incomingByte == '1'){
      digitalWrite(led, HIGH);
      Serial.write("ON");      
    }
    else if(incomingByte == '0'){
      digitalWrite(led,LOW);
      Serial.write("OFF");
    }
  }
}
