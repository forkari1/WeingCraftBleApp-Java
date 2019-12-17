#define SwichingMoter 8
#define StartMoter 9 
#define stepsPer 1200

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600); // 9600??? ???????

  pinMode(StartMoter,OUTPUT);
  pinMode(SwichingMoter,OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:

  if(Serial.available()){ //?????? ????
    char move = Serial.read(); // ?????? ??? ????
    Serial.println(move);

    if(move=='2' || move=="내려가"){ // ???? ?? 2?? ??? ??
      Serial.write(move);
      digitalWrite(SwichingMoter, LOW);
      for(int x=0; x<stepsPer; x++)
      {
        digitalWrite(StartMoter, HIGH);
        delayMicroseconds(1000);
        digitalWrite(StartMoter, LOW);
        delayMicroseconds(1000);
      }
    }
    else if(move=='1' || move=="올라가"){ // ???? ?? 1?? ??? ??
      Serial.write(move);
      digitalWrite(SwichingMoter, HIGH);
      for(int x=0; x<stepsPer; x++)
      {
        digitalWrite(StartMoter, HIGH);
        delayMicroseconds(1000);
        digitalWrite(StartMoter, LOW);
        delayMicroseconds(1000);
      } 
        } 
  }
}
