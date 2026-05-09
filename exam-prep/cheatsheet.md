# AVL Tree & BST Cheatsheet

## AVL Tree
- **개념**: 자가 균형 이진 탐색 트리 (Self-Balancing Binary Search Tree)
- **속성**: 모든 노드의 `balance factor` (왼쪽 서브트리 높이 - 오른쪽 서브트리 높이)가 -1, 0, 1 중 하나.
- **높이**: O(log n) 보장
- **핵심 연산**:
    - `getBalance(Node* node)`: `height(node->left) - height(node->right)`
    - `updateHeight(Node* node)`: `1 + max(getHeight(node->left), getHeight(node->right))`
    - **회전 (Rotation)**: 트리의 균형을 맞추기 위한 연산.
        - **LL (Right Rotate)**: `balance > 1` 이고 `key < node->left->key`
        - **RR (Left Rotate)**: `balance < -1` 이고 `key > node->right->key`
        - **LR (Left-Right Rotate)**: `balance > 1` 이고 `key > node->left->key` (왼쪽 자식에 대해 `leftRotate` 후 현재 노드에 대해 `rightRotate`)
        - **RL (Right-Left Rotate)**: `balance < -1` 이고 `key < node->right->key` (오른쪽 자식에 대해 `rightRotate` 후 현재 노드에 대해 `leftRotate`)
- **주의**: 회전 후 관련 노드들의 `height` 반드시 업데이트.

## Binary Search Tree (BST)
- **속성**: `왼쪽 자식 < 부모 < 오른쪽 자식`
- **중위 순회 (Inorder Traversal)**: 정렬된 데이터를 얻음.
- **시간 복잡도**:
    - **평균**: O(log n)
    - **최악**: O(n) (편향 트리, skew tree)
- **삭제 시나리오**:
    1.  **리프 노드**: 그냥 삭제
    2.  **자식 1개**: 자식을 부모에 연결 후 삭제
    3.  **자식 2개**: 중위 후속자(inorder successor)로 대체 후 삭제

## C++ 코드 팁
- `getHeight` 함수는 `nullptr` 체크 필수 (0 반환).
- `max` 함수 활용 (`std::max`).
- `insert` 함수에서 중복 키 처리.
