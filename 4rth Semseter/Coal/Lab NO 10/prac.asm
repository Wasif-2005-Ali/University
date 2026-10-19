; Move digits into registers
mov AL, 24          ; AL = 24 (0x18)
mov BX, 51          ; BX = 51 (0x33)
mov CL, 87          ; CL = 87 (0x57)

; Apply MOVZX on AL → EAX
MOVZX EAX, AL       ; EAX = 0x00000018 = 24

; Apply MOVSX on CL → ECX
MOVSX ECX, CL       ; CL = 87 = 0x57 (positive, MSB=0)
                    ; ECX = 0x00000057 = 87