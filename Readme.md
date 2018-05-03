-- 정보과학회
효율적인 셔틀 공유서비스를 위한 차량 라우팅
An efficient routing for shuttle sharing

프로그램0: 라우팅 알고리즘 
…. 이번에는 제외

프로그램 1: 셔틀 시뮬레이터
목표: 탑승자와 셔틀정보가 정해졌을 때..

0.	Station..
1.	Generator
Guest(release time, deadline, source, destination)
1) uniform
2) random… normal
input: csv, station들의 좌표, guest mean, stdev 
source, destination모두 정해진 위치에서만 나올 수 있도록..

2.	 Shuttle및 탑승 정책
generate routing table: (place, depart time)
방법1: csv 인풋( 고정 rounting table이용) (naiive)
방법2: csv base인데, 급행 도입. (naiive2)
방법3: csv base인데, 셔틀을 골라 탈 수 있다.

3.	탑승 정책 (가장 기본)
환승안함. 시작과 내리는 station 정해짐 (deterministic)

4.	Simulator
다음 데이터 수집 가능하도록..
- 각 사람별로 얼마나 기다렸는지(wait time)
- 원하는 도착시간에 맞추어졌는지 (deadline miss ratio)
- 버스안 최대 인원 몇 명이었는지 (peak density)
- 버스의 총 이동거리 
구현방법:
시뮬레이션 시작 ~ 끝을 정해주면, 타임 틱을 증가시켜 가면서.. 셔틀 탑승/등 구현


	Place (x,y) 2차원 좌표.

프로그램2: visualization 툴


