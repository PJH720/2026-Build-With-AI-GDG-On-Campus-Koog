## Week 8: AVL 트리 복습 노트

### 1. AVL 트리 필요성
*   **문제점**: BST는 최악의 경우 편향 트리(skewed tree)가 되어 삽입/삭제/탐색 성능이 O(n)까지 저하될 수 있음.
*   **해결책**: AVL 트리는 자동 밸런싱 기능을 통해 트리의 균형을 유지하여, 항상 **O(log n)**의 성능을 보장.

### 2. 밸런스 팩터 (Balance Factor, BF)
*   **정의**: `BF(node) = height(left_subtree) - height(right_subtree)`
*   **균형 조건**: 트리의 모든 노드에 대해 `|BF| <= 1` 이어야 함.
*   **불균형**: `|BF| > 1` 이면 트리가 불균형 상태이며, 균형을 맞추기 위한 **회전(Rotation)**이 필요.

### 3. 4가지 회전 (Rotation) 유형
트리의 불균형 유형에 따라 다음 4가지 회전 중 하나를 수행하여 균형을 맞춤.
*   **LL (Left-Left) 불균형**: 왼쪽 자식의 왼쪽에 노드가 삽입되어 불균형 발생.
    *   **회전**: **Right Rotation** (오른쪽 회전)
*   **RR (Right-Right) 불균형**: 오른쪽 자식의 오른쪽에 노드가 삽입되어 불균형 발생.
    *   **회전**: **Left Rotation** (왼쪽 회전)
*   **LR (Left-Right) 불균형**: 왼쪽 자식의 오른쪽에 노드가 삽입되어 불균형 발생.
    *   **회전**: **Left Rotation → Right Rotation** (좌-우 회전)
*   **RL (Right-Left) 불균형**: 오른쪽 자식의 왼쪽에 노드가 삽입되어 불균형 발생.
    *   **회전**: **Right Rotation → Left Rotation** (우-좌 회전)

### 4. BST vs AVL 비교
*   **균형 보장**:
    *   BST: X (최악의 경우 편향 트리)
    *   AVL: O (항상 균형 유지)
*   **삽입/삭제/탐색 성능**:
    *   BST: O(n) (최악)
    *   AVL: **O(log n)** (항상 보장)
*   **구현 복잡도**:
    *   BST: 단순
    *   AVL: 복잡 (회전 로직 및 높이 업데이트 필요)

### 5. 핵심 키워드
*   **밸런스 팩터 (Balance Factor)**
*   **LL/RR/LR/RL 회전**
*   **높이 업데이트**
*   **자동 밸런싱**