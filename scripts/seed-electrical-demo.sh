#!/usr/bin/env bash
set -euo pipefail

# Creates a fresh, detailed electrical-demo scope owned by Alice.
# Requires: curl, jq. Override PERSIQA_BASE_URL, PERSIQA_USER, or PERSIQA_PASSWORD as needed.
base_url="${PERSIQA_BASE_URL:-http://localhost:8080}"
user="${PERSIQA_USER:-alice}"
password="${PERSIQA_PASSWORD:-alice}"
api="$base_url/api"
scope_name="Electrical system - Apartment detailed $(date +%Y%m%d-%H%M%S)"

post() {
  curl --silent --show-error --fail-with-body --user "$user:$password" \
    --header 'Content-Type: application/json' --request POST "$1" --data "$2"
}

scope_id=$(post "$api/scopes" "{\"name\":\"$scope_name\"}" | jq -r '.id')
create_node() { post "$api/scopes/$scope_id/nodes" "{\"id\":\"$1\",\"kind\":\"ENTITY\"}" >/dev/null; }
relation() {
  post "$api/scopes/$scope_id/statements" "{\"knowledgeKind\":\"EXPLICIT\",\"relationType\":\"$2\",\"source\":{\"id\":\"$1\"},\"target\":{\"id\":\"$3\",\"kind\":\"$4\"},\"derivedFrom\":[],\"context\":{\"provenance\":\"electrical demo seed\",\"scenario\":\"as installed\",\"confidence\":0.97}}" >/dev/null
}

for node in UtilityGrid-01 MainDistributionBoard-01 MainSwitch-01 SurgeProtectionDevice-01 RCD-WetAreas-01 RCD-General-01 MCB-KitchenSockets-01 MCB-BathroomSockets-01 MCB-Lighting-01 MCB-Boiler-01 KitchenSocketCircuit-01 BathroomSocketCircuit-01 LightingCircuit-01 BoilerCircuit-01 JunctionBox-Kitchen-01 JunctionBox-Bathroom-01 KitchenOutlet-01 KitchenOutlet-02 BathroomOutlet-01 Dishwasher-01 WashingMachine-01 KitchenCeilingLight-01 HallCeilingLight-01 KitchenWallSwitch-01 HallWallSwitch-01 ElectricBoiler-01 Router-01; do create_node "$node"; done

for edge in \
  'UtilityGrid-01|supplies|MainSwitch-01|ENTITY' 'MainSwitch-01|supplies|SurgeProtectionDevice-01|ENTITY' \
  'SurgeProtectionDevice-01|supplies|RCD-WetAreas-01|ENTITY' 'SurgeProtectionDevice-01|supplies|RCD-General-01|ENTITY' \
  'RCD-WetAreas-01|supplies|MCB-KitchenSockets-01|ENTITY' 'RCD-WetAreas-01|supplies|MCB-BathroomSockets-01|ENTITY' \
  'RCD-General-01|supplies|MCB-Lighting-01|ENTITY' 'RCD-General-01|supplies|MCB-Boiler-01|ENTITY' \
  'MCB-KitchenSockets-01|supplies|KitchenSocketCircuit-01|ENTITY' 'MCB-BathroomSockets-01|supplies|BathroomSocketCircuit-01|ENTITY' \
  'MCB-Lighting-01|supplies|LightingCircuit-01|ENTITY' 'MCB-Boiler-01|supplies|BoilerCircuit-01|ENTITY' \
  'MCB-Boiler-01|supplies|JunctionBox-Boiler-01|ENTITY' 'JunctionBox-Boiler-01|supplies|BoilerCircuit-01|ENTITY' \
  'KitchenSocketCircuit-01|supplies|JunctionBox-Kitchen-01|ENTITY' 'JunctionBox-Kitchen-01|supplies|KitchenOutlet-01|ENTITY' 'JunctionBox-Kitchen-01|supplies|KitchenOutlet-02|ENTITY' \
  'KitchenSocketCircuit-01|supplies|Dishwasher-01|ENTITY' 'KitchenSocketCircuit-01|supplies|WashingMachine-01|ENTITY' \
  'BathroomSocketCircuit-01|supplies|JunctionBox-Bathroom-01|ENTITY' 'JunctionBox-Bathroom-01|supplies|BathroomOutlet-01|ENTITY' \
  'LightingCircuit-01|supplies|KitchenCeilingLight-01|ENTITY' 'LightingCircuit-01|supplies|HallCeilingLight-01|ENTITY' 'BoilerCircuit-01|supplies|ElectricBoiler-01|ENTITY' 'KitchenOutlet-02|supplies|Router-01|ENTITY' \
  'MainDistributionBoard-01|contains|MainSwitch-01|ENTITY' 'MainDistributionBoard-01|contains|SurgeProtectionDevice-01|ENTITY' 'MainDistributionBoard-01|contains|RCD-WetAreas-01|ENTITY' 'MainDistributionBoard-01|contains|RCD-General-01|ENTITY' \
  'MainDistributionBoard-01|contains|MCB-KitchenSockets-01|ENTITY' 'MainDistributionBoard-01|contains|MCB-BathroomSockets-01|ENTITY' 'MainDistributionBoard-01|contains|MCB-Lighting-01|ENTITY' 'MainDistributionBoard-01|contains|MCB-Boiler-01|ENTITY' \
  'KitchenWallSwitch-01|connectedTo|KitchenCeilingLight-01|ENTITY' 'HallWallSwitch-01|connectedTo|HallCeilingLight-01|ENTITY' 'KitchenCeilingLight-01|dependsOn|KitchenWallSwitch-01|ENTITY' 'HallCeilingLight-01|dependsOn|HallWallSwitch-01|ENTITY' \
  'MainSwitch-01|hasState|MainSwitch-01-On|STATE' 'RCD-WetAreas-01|hasState|RCD-WetAreas-01-On|STATE' 'MCB-KitchenSockets-01|hasState|MCB-KitchenSockets-01-On|STATE' 'KitchenWallSwitch-01|hasState|KitchenWallSwitch-01-Off|STATE' 'KitchenCeilingLight-01|hasState|KitchenCeilingLight-01-Off|STATE' \
  'MainDistributionBoard-01|hasCapability|CircuitDistribution|CAPABILITY' 'RCD-WetAreas-01|hasCapability|ResidualCurrentProtection|CAPABILITY' 'MCB-KitchenSockets-01|instanceOf|MiniatureCircuitBreaker|CONCEPT' 'RCD-WetAreas-01|instanceOf|ResidualCurrentDevice|CONCEPT' 'MainDistributionBoard-01|playsRole|ApartmentElectricalDistribution|CONCEPT'; do
  IFS='|' read -r source type target kind <<< "$edge"; relation "$source" "$type" "$target" "$kind"
done

curl --silent --show-error --fail-with-body --user "$user:$password" "$api/scopes/$scope_id/knowledge" | jq --arg scope_id "$scope_id" '{scopeId: $scope_id, scope: .scope.name, nodes: (.nodes | length), relations: (.relations | length), statements: (.statements | length)}'
