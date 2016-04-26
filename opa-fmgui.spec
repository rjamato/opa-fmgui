#
# spec file for package opa-fmgui
#
# Copyright (c) 2015 Intel Corporation
#
%global appfolder opa-fmgui
%global appjar opa-fmgui.jar

Name:           opa-fmgui
Version:        10.0.0.0.3
Release:        1%{?dist}
Summary:        Intel Omni-Path Architecture Fabric Manager Graphical User Interface
Group:          Applications/System
# The entire source code is BDS except Gritty which is LGPLv2+
License:        BSD
URL:            www.intel.com
Source0:        opa-fmgui-10.0.0.0.3.tar.gz 
BuildArch:      noarch

BuildRequires: gradle-local
BuildRequires: maven-local
BuildRequires: javapackages-local
BuildRequires: javapackages-tools
BuildRequires: ant
BuildRequires: mvn(log4j:log4j)
BuildRequires: mvn(org.hibernate.common:hibernate-commons-annotations)
BuildRequires: mvn(org.hibernate:hibernate-core)
BuildRequires: mvn(org.hibernate:hibernate-entitymanager)
BuildRequires: mvn(org.hibernate.javax.persistence:hibernate-jpa-2.1-api)
BuildRequires: mvn(org.hsqldb:hsqldb)
BuildRequires: mvn(org.jboss:jandex)
BuildRequires: mvn(com.sun.mail:javax.mail)
BuildRequires: mvn(org.slf4j:log4j-over-slf4j)
BuildRequires: mvn(org.slf4j:slf4j-api)
BuildRequires: mvn(ch.qos.logback:logback-classic)
BuildRequires: mvn(ch.qos.logback:logback-core)
BuildRequires: mvn(org.jfree:jfreechart)
BuildRequires: mvn(org.swinglabs.swingx:swingx-all)
BuildRequires: mvn(org.jfree:jcommon)
BuildRequires: mvn(javax.help:javahelp)
BuildRequires: mvn(org.jboss.logging:jboss-logging)
BuildRequires: mvn(org.jboss.logging:jboss-logging-annotations)
BuildRequires: mvn(org.jboss.spec.javax.transaction:jboss-transaction-api_1.2_spec)
BuildRequires: mvn(org.javassist:javassist)
BuildRequires: mvn(dom4j:dom4j)
BuildRequires: mvn(antlr:antlr)
BuildRequires: mvn(com.jcraft:jsch)
BuildRequires: mvn(net.engio:mbassador)
BuildRequires: mvn(com.mxgraph:jgraphx)
BuildRequires: desktop-file-utils

Requires: jre >= 1.7

# Filter all jar files, except "gritty.jar".
%global __provides_exclude_from ^%{_javadir}/%{appfolder}/lib/[^(gritty)].*.jar$

%description
OPA-FMGUI is the Intel Omni-Path Architecture Fabric Manager Graphical User Interface. It can be run by
invoking the Bash script fmgui.

%prep
%setup  -q

%build
%if 0%{?fedora} >= 24
    %gradle_build -i copyDeps
%else
    gradle --offline copyDeps build
%endif

%install
%mvn_install

%doc THIRD-PARTY-README
%doc Third_Party_Copyright_Notices_and_Licenses.txt

install -m 755 -pDt %{buildroot}/%{_javadir}/%{appfolder} %{appjar}
install -m 644 -pDt %{buildroot}/%{_javadir}/%{appfolder} THIRD-PARTY-README
install -m 644 -pDt %{buildroot}/%{_javadir}/%{appfolder} Third_Party_Copyright_Notices_and_Licenses.txt
# All jar files provided by other RPMs had been prevented from scanning for deps.
install -m 755 -pDt %{buildroot}/%{_javadir}/%{appfolder}/lib lib/*
install -m 755 -pDt %{buildroot}/%{_javadir}/%{appfolder}/third_party_licenses lib/*
install -m 755 -pDt %{buildroot}/%{_javadir}/%{appfolder}/help target/help/*
install -m 644 -pDt %{buildroot}/%{_javadir}/%{appfolder}/help help/*.html 
install -m 644 -pDt %{buildroot}/%{_javadir}/%{appfolder}/help help/LICENSE
install -m 755 -pDt %{buildroot}/%{_javadir}/%{appfolder}/util util/fmguiclear.sh
install -m 755 -pDt %{buildroot}/%{_javadir}/%{appfolder}/util util/postsetup.sh
install -m 644 -pDt %{buildroot}/%{_javadir}/%{appfolder}/util util/ClearFMGUICache.desktop
install -m 755 -pD  install/fmgui.sh %{buildroot}/%{_bindir}/opa-fmgui
install -m 644 -pDt %{buildroot}/%{_sysconfdir}/profile.d install/fmguivars.sh
install -m 644 -pDt %{buildroot}/%{_sysconfdir}/xdg/menus/applications-merged install/Fabric.menu
install -m 644 -pDt %{buildroot}%{_datadir}/desktop-directories install/Fabric.directory
for file in $(find install/images -type f); do
    install -m 644 -pDt %{buildroot}/%{_datadir}/icons/hicolor $file
done
desktop-file-install --dir=%{buildroot}/%{_datadir}/applications install/fmgui.desktop

%post
/bin/touch --no-create %{_datadir}/icons/hicolor &>/dev/null || :

%postun
if [ $1 -eq 0 ] ; then
    /bin/touch --no-create %{_datadir}/icons/hicolor &>/dev/null
    /usr/bin/gtk-update-icon-cache %{_datadir}/icons/hicolor &>/dev/null || :
fi

%posttrans
/usr/bin/gtk-update-icon-cache %{_datadir}/icons/hicolor &>/dev/null || :

%files
%{_javadir}/%{appfolder}
%{_bindir}/fmgui
%{_datadir}/applications/fmgui.desktop
%{_datadir}/desktop-directories/Fabric.directory
%{_datadir}/icons/hicolor
%config(noreplace) %{_sysconfdir}/xdg/menus/applications-merged/Fabric.menu
%config(noreplace) %{_sysconfdir}/profile.d/fmguivars.sh
%license LICENSE

%changelog
* Tue Apr 26 2016 Robert Amato <robert.amato@intel.com> - 10.0.0.0.3-3
- Fix review issues
