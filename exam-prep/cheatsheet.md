# 이진 탐색 트리 (BST) 및 AVL 트리 치트시트

## 1. 이진 탐색 트리 (BST) 핵심 요약
*   **속성**: 왼쪽 자식 < 부모 < 오른쪽 자식
*   **중위 순회**: 정렬된 결과를 반환
*   **시간 복잡도**:
    *   **평균**: O(log n)
    *   **최악**: O(n) (편향 트리일 경우)
*   **삭제 시나리오**:
    1.  **리프 노드**: 바로 삭제
    2.  **자식 1개**: 자식을 부모에 연결
    3.  **자식 2개**: 중위 후속자(inorder successor)로 대체 후 삭제

## 2. AVL 트리 핵심 요약
*   **필요성**: BST의 최악의 경우 O(n) 성능 저하를 해결하기 위해 자동 밸런싱 도입.
*   **특징**: 항상 O(log n) 성능 보장.
*   **밸런스 팩터 (BF)**:
    *   정의: `BF(node) = height(left_subtree) - height(right_subtree)`
    *   균형 조건: 모든 노드에 대해 `|BF| <= 1`
    *   불균형: `|BF| > 1` → 회전(Rotation) 필요
*   **4가지 회전 유형**:
    1.  **LL (Left-Left) 불균형**: `Right Rotation`
    2.  **RR (Right-Right) 불균형**: `Left Rotation`
    3.  **LR (Left-Right) 불균형**: `Left Rotation` 후 `Right Rotation`
    4.  **RL (Right-Left) 불균형**: `Right Rotation` 후 `Left Rotation`
*   **구현 시 주의사항**:
    *   `getBalance` 함수 일관된 계산 (예: `left - right`)
    *   회전 후 관련 노드들의 `height` 업데이트 필수

## 3. BST vs AVL 트리 비교
| 특징         | 이진 탐색 트리 (BST) | AVL 트리                   |
| :----------- | :------------------- | :------------------------- |
| **균형 보장**  | X (편향될 수 있음)   | O (항상 균형 유지)         |
| **성능**     | O(n) (최악)          | **O(log n)** (항상 보장)   |
| **구현 복잡도**| 단순                 | 복잡 (회전 로직, 높이 업데이트) |

## 4. AVL 트리 구현 가이드 (주요 코드 수정 요약)
*   `getBalance` 함수: `height(left) - height(right)`로 통일.
*   `rightRotate` 및 `leftRotate` 함수: 회전 후 `height` 업데이트 로직 추가.
*   `insert` 함수: `getBalance` 결과와 삽입된 키 위치에 따라 정확한 회전 케이스 적용.

**테스트 케이스 예시**:
*   **입력**: 10, 20, 30, 40, 50, 25
*   **기대 출력**: `Inorder traversal: 10 20 25 30 40 50`