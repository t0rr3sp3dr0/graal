/*
 * Copyright (c) 2017, Oracle and/or its affiliates.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided
 * with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.oracle.truffle.llvm.runtime.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class LLVMDebugStructLikeType extends LLVMDebugType {

    private final List<LLVMDebugMemberType> members;

    public LLVMDebugStructLikeType(long size, long align, long offset) {
        super(size, align, offset);
        this.members = new ArrayList<>();
    }

    private LLVMDebugStructLikeType(Supplier<String> name, long size, long align, long offset, List<LLVMDebugMemberType> members) {
        super(size, align, offset);
        setName(name);
        this.members = members;
    }

    public void addMember(LLVMDebugMemberType member) {
        members.add(member);
    }

    @Override
    public LLVMDebugType getOffset(long newOffset) {
        return new LLVMDebugStructLikeType(this::getName, getSize(), getAlign(), newOffset, members);
    }

    @Override
    public boolean isAggregate() {
        return true;
    }

    @Override
    public int getElementCount() {
        return members.size();
    }

    @Override
    public String getElementName(long i) {
        if (0 <= i && i < members.size()) {
            return members.get((int) i).getName();
        }
        return null;
    }

    @Override
    public LLVMDebugType getElementType(long i) {
        if (0 <= i && i < members.size()) {
            return members.get((int) i).getOffsetElementType();
        }
        return null;
    }

    @Override
    public LLVMDebugType getElementType(String name) {
        if (name == null) {
            return null;
        }
        for (final LLVMDebugMemberType member : members) {
            if (name.equals(member.getName())) {
                return member.getOffsetElementType();
            }
        }
        return null;
    }
}
