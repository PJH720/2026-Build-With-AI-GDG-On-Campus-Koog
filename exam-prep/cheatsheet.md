# 자료구조 시험 대비 치트시트: BST & AVL 트리

## 1. 이진 탐색 트리 (Binary Search Tree, BST)

*   **정의**: 왼쪽 자식 < 부모 < 오른쪽 자식 속성을 만족하는 이진 트리
*   **주요 특징**:
    *   중위 순회 시 정렬된 결과 반환
    *   **시간 복잡도**:
        *   평균: O(log N) (탐색, 삽입, 삭제)
        *   최악: O(N) (편향 트리일 경우)
*   **노드 삭제 시나리오**:
    1.  **리프 노드**: 바로 삭제
    2.  **자식 1개**: 자식을 부모에 연결 후 삭제
    3.  **자식 2개**: 중위 후속자(inorder successor)로 대체 후 삭제

## 2. AVL 트리

*   **정의**: 스스로 균형을 맞추는 이진 탐색 트리 (Self-Balancing BST)
*   **필요성**: BST의 최악의 경우 O(N) 성능 문제를 해결, 모든 연산을 O(log N)으로 보장
*   **밸런스 팩터 (Balance Factor, BF)**:
    *   `BF(node) = height(왼쪽 서브트리) - height(오른쪽 서브트리)`
    *   **균형 조건**: 모든 노드의 `|BF| <= 1`
    *   `|BF| > 1` 일 경우, 불균형 발생 → 회전 필요
*   **4가지 회전 (Rotation)**:
    1.  **LL (Left-Left) 불균형**: `Right Rotation` (오른쪽 회전)
    2.  **RR (Right-Right) 불균형**: `Left Rotation` (왼쪽 회전)
    3.  **LR (Left-Right) 불균형**: `Left Rotation` 후 `Right Rotation` (좌회전 후 우회전)
    4.  **RL (Right-Left) 불균형**: `Right Rotation` 후 `Left Rotation` (우회전 후 좌회전)
*   **구현 시 주의사항**:
    *   `getBalance` 함수 정의 통일 (`height(left) - height(right)`)
    *   회전 후 관련 노드들의 `height` 값 반드시 업데이트
*   **BST vs AVL 트리 비교**:
    | 구분 | BST | AVL 트리 |
    | :--- | :--- | :--- |
    | 균형 보장 | X | O |
    | 삽입/삭제 성능 | 최악 O(N) | 항상 O(log N) |
    | 구현 복잡도 | 단순 | 복잡 |