; De Morgan
{:name "not-forall->exists-not", :given [(not (forall [x] (P x)))], :conclusion [(exists [x] (not (P x)))], :forwards true, :proof [{:id 1, :body (not (forall [x] (P x))), :rule :premise} [{:id 4, :body (not (exists [x] (not (P x)))), :rule :assumption} [{:id 9, :body (actual :i), :rule :assumption} [{:id 12, :body (not (P :i)), :rule :assumption} {:id 16, :body (exists [x] (not (P x))), :rule "\"exists-i\" (12 9) []"} {:id 14, :body contradiction, :rule "\"not-e\" (16 4) []"}] {:id 11, :body (P :i), :rule "\"raa\" ([12 14]) []"}] {:id 8, :body (forall [x] (P x)), :rule "\"forall-i\" ([9 11]) []"} {:id 6, :body contradiction, :rule "\"not-e\" (8 1) []"}] {:id 3, :body (exists [x] (not (P x))), :rule "\"raa\" ([4 6]) []"}]}
{:name "not-exists->forall-not", :given [(not (exists [x] (P x)))], :conclusion [(forall [x] (not (P x)))], :forwards true, :proof [{:id 1, :body (not (exists [x] (P x))), :rule :premise} [{:id 4, :body (actual :i), :rule :assumption} [{:id 7, :body (P :i), :rule :assumption} {:id 11, :body (exists [x] (P x)), :rule "\"exists-i\" (7 4) []"} {:id 9, :body contradiction, :rule "\"not-e\" (11 1) []"}] {:id 6, :body (not (P :i)), :rule "\"not-i\" ([7 9]) []"}] {:id 3, :body (forall [x] (not (P x))), :rule "\"forall-i\" ([4 6]) []"}]}
{:name "exists-not->not-forall", :given [(exists [x] (not (P x)))], :conclusion [(not (forall [x] (P x)))], :forwards true, :proof [{:id 1, :body (exists [x] (not (P x))), :rule :premise} [{:id 4, :body (forall [x] (P x)), :rule :assumption} [{:rule :assumption, :id 7, :body (actual :i)} {:rule :assumption, :id 8, :body (not (P :i))} {:id 11, :body (P :i), :rule "\"forall-e\" (4 7) []"} {:id 12, :body contradiction, :rule "\"not-e\" (8 11) []"}] {:id 6, :body contradiction, :rule "\"exists-e\" ([7 12] 1) []"}] {:id 3, :body (not (forall [x] (P x))), :rule "\"not-i\" ([4 6]) []"}]}
{:name "forall-not->not-exists", :given [(forall [x] (not (P x)))], :conclusion [(not (exists [x] (P x)))], :forwards true, :proof [{:id 1, :body (forall [x] (not (P x))), :rule :premise} [{:id 4, :body (exists [x] (P x)), :rule :assumption} [{:rule :assumption, :id 7, :body (actual :i)} {:rule :assumption, :id 8, :body (P :i)} {:id 11, :body (not (P :i)), :rule "\"forall-e\" (1 7) []"} {:id 12, :body contradiction, :rule "\"not-e\" (8 11) []"}] {:id 6, :body contradiction, :rule "\"exists-e\" ([7 12] 4) []"}] {:id 3, :body (not (exists [x] (P x))), :rule "\"not-i\" ([4 6]) []"}]}
; Modus Barbara
{:name "modus-barbara", :given [(forall [x] (impl (P x) (Q x))) (forall [x] (impl (Q x) (R x)))], :conclusion [(forall [x] (impl (P x) (R x)))], :forwards true, :proof [{:rule :premise, :id 1, :body (forall [x] (impl (P x) (Q x)))} {:rule :premise, :id 2, :body (forall [x] (impl (Q x) (R x)))} [{:id 5, :body (actual :i), :rule :assumption} {:id 8, :body (impl (P :i) (Q :i)), :rule "\"forall-e\" (1 5) []"} {:id 9, :body (impl (Q :i) (R :i)), :rule "\"forall-e\" (2 5) []"} [{:id 10, :body (P :i), :rule :assumption} {:id 13, :body (Q :i), :rule "\"impl-e\" (8 10) []"} {:id 14, :body (R :i), :rule "\"impl-e\" (9 13) []"}] {:id 7, :body (impl (P :i) (R :i)), :rule "\"impl-i\" ([10 14]) []"}] {:id 4, :body (forall [x] (impl (P x) (R x))), :rule "\"forall-i\" ([5 7]) []"}]}
{:name "modus-celarent", :given [(forall [x] (impl (P x) (not (Q x)))) (forall [x] (impl (R x) (P x)))], :conclusion [(forall [x] (impl (R x) (not (Q x))))], :forwards true, :proof [{:rule :premise, :id 1, :body (forall [x] (impl (P x) (not (Q x))))} {:rule :premise, :id 2, :body (forall [x] (impl (R x) (P x)))} [{:id 5, :body (actual :i), :rule :assumption} {:id 8, :body (impl (P :i) (not (Q :i))), :rule "\"forall-e\" (1 5) []"} {:id 9, :body (impl (R :i) (P :i)), :rule "\"forall-e\" (2 5) []"} [{:id 10, :body (R :i), :rule :assumption} {:id 13, :body (P :i), :rule "\"impl-e\" (9 10) []"} {:id 14, :body (not (Q :i)), :rule "\"impl-e\" (8 13) []"}] {:id 7, :body (impl (R :i) (not (Q :i))), :rule "\"impl-i\" ([10 14]) []"}] {:id 4, :body (forall [x] (impl (R x) (not (Q x)))), :rule "\"forall-i\" ([5 7]) []"}]}
