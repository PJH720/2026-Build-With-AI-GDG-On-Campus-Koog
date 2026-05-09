# 과제 3: AVL Tree 구현 가이드

## 1. 핵심 개념 요약

AVL 트리는 **자가 균형 이진 탐색 트리**입니다. 모든 노드에서 왼쪽 서브트리의 높이와 오른쪽 서브트리의 높이 차이(밸런스 팩터)가 -1, 0, 1 중 하나를 유지하여 트리의 높이를 O(log n)으로 보장합니다.

## 2. 학생 코드 분석 및 수정 가이드

`student-code/avl_tree.cpp` 파일의 주요 수정 사항 및 가이드입니다.

### 2.1. `getBalance` 함수

**한 줄 요약:** 밸런스 팩터 계산 순서를 `left - right`로 통일하거나, 현재 `right - left` 방식에 맞춰 회전 조건을 정확히 재설정해야 합니다. (여기서는 `left - right`로 통일하는 것을 권장)

**상세 설명:**
일반적으로 밸런스 팩터는 `height(left) - height(right)`로 계산합니다. 학생 코드는 `height(right) - height(left)`로 계산하고 있어, 회전 조건을 반대로 해석해야 하는 혼동의 여지가 있습니다. `left - right`로 통일하는 것이 직관적이며 오류를 줄일 수 있습니다.

**수정 코드 (C++):**

```cpp
int getBalance(Node* node) {
    if (node == nullptr) return 0;
    // 왼쪽 - 오른쪽으로 변경
    return getHeight(node->left) - getHeight(node->right);
}
```

### 2.2. `rightRotate` 및 `leftRotate` 함수

**한 줄 요약:** 회전 후 `y`와 `x` 노드의 높이를 반드시 업데이트해야 합니다.

**상세 설명:**
AVL 트리에서는 노드의 높이가 밸런스 팩터 계산에 중요하게 사용되므로, 회전으로 인해 트리의 구조가 변경되면 관련 노드들의 높이를 최신 상태로 갱신해야 합니다. `rightRotate` 함수에 높이 업데이트 로직이 누락되어 있습니다. `leftRotate`는 잘 구현되어 있습니다.

**`rightRotate` 수정 코드 (C++):**

```cpp
Node* rightRotate(Node* y) {
    Node* x = y->left;
    Node* T2 = x->right;

    x->right = y;
    y->left = T2;

    // 높이 업데이트 추가
    y->height = max(getHeight(y->left), getHeight(y->right)) + 1;
    x->height = max(getHeight(x->left), getHeight(x->right)) + 1;

    return x;
}
```

### 2.3. `insert` 함수 내 회전 로직

**한 줄 요약:** `getBalance` 함수를 `left - right` 방식으로 수정한 후, 각 회전 케이스의 밸런스 조건과 삽입된 `key`의 위치 조건을 정확히 일치시켜야 합니다.

**상세 설명:**
`getBalance`를 `height(left) - height(right)`로 수정했다고 가정하고 회전 조건을 다시 작성합니다.

*   `balance > 1`: 왼쪽 서브트리가 더 높음 (LL 또는 LR 케이스)
*   `balance < -1`: 오른쪽 서브트리가 더 높음 (RR 또는 RL 케이스)

**`insert` 함수 수정 코드 (C++):**

```cpp
Node* insert(Node* node, int key) {
    if (node == nullptr) return new Node(key);

    if (key < node->key)
        node->left = insert(node->left, key);
    else if (key > node->key)
        node->right = insert(node->right, key);
    else // 중복 키는 허용하지 않음
        return node;

    // 현재 노드의 높이 업데이트
    node->height = 1 + max(getHeight(node->left), getHeight(node->right));

    int balance = getBalance(node);

    // LL Case: 왼쪽 서브트리가 높고, 새 노드가 왼쪽 자식의 왼쪽에 삽입됨
    if (balance > 1 && key < node->left->key)
        return rightRotate(node);

    // RR Case: 오른쪽 서브트리가 높고, 새 노드가 오른쪽 자식의 오른쪽에 삽입됨
    if (balance < -1 && key > node->right->key)
        return leftRotate(node);

    // LR Case: 왼쪽 서브트리가 높고, 새 노드가 왼쪽 자식의 오른쪽에 삽입됨
    if (balance > 1 && key > node->left->key) {
        node->left = leftRotate(node->left);
        return rightRotate(node);
    }

    // RL Case: 오른쪽 서브트리가 높고, 새 노드가 오른쪽 자식의 왼쪽에 삽입됨
    if (balance < -1 && key < node->right->key) {
        node->right = rightRotate(node->right);
        return leftRotate(node);
    }

    return node;
}
```

## 3. 테스트 및 검증

수정된 코드를 컴파일(`g++ -o avl avl_tree.cpp`)하고 실행하여 다음 테스트 케이스를 검증합니다.

**입력:** 10, 20, 30, 40, 50, 25
**기대 출력:** `Inorder traversal: 10 20 25 30 40 50`

이 가이드를 통해 과제를 성공적으로 완료하시길 바랍니다.
